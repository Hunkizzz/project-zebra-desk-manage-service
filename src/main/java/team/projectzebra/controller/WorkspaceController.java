package team.projectzebra.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.projectzebra.dao.ReservationLogDao;
import team.projectzebra.dto.WorkspaceInfoDto;
import team.projectzebra.dto.WorkspaceStatus;
import team.projectzebra.enums.WorkspaceType;
import team.projectzebra.persistence.entity.ReservationLog;
import team.projectzebra.persistence.entity.Workspace;
import team.projectzebra.persistence.entity.WorkspaceRestriction;
import team.projectzebra.persistence.repository.CompanyRepository;
import team.projectzebra.persistence.repository.ReservationLogRepository;
import team.projectzebra.persistence.repository.WorkspaceRepository;
import team.projectzebra.persistence.repository.WorkspaceRestrictionRepository;
import team.projectzebra.rabbitmq.Producer;
import team.projectzebra.util.exceptions.ResourceNotFoundException;
import team.projectzebra.util.exceptions.ValidationFailedException;

@Api(value = "Workspaces Booking Management System")
@RestController
@RequestMapping("/api/v1/projectzebrateam-workspace-reservation-service")
public class WorkspaceController {
    WorkspaceRepository workspaceRepository;
    CompanyRepository companyRepository;
    ReservationLogRepository reservationLogRepository;
    WorkspaceRestrictionRepository workspaceRestrictionRepository;
    Producer producer;

    private static final Logger logger = LoggerFactory.getLogger(WorkspaceController.class);

    @Autowired
    public WorkspaceController(WorkspaceRepository workspaceRepository,
                               CompanyRepository companyRepository,
                               ReservationLogRepository reservationLogRepository,
                               WorkspaceRestrictionRepository workspaceRestrictionRepository,
                               Producer producer) {
        this.workspaceRepository = workspaceRepository;
        this.companyRepository = companyRepository;
        this.reservationLogRepository = reservationLogRepository;
        this.workspaceRestrictionRepository = workspaceRestrictionRepository;
        this.producer = producer;
    }

    @ApiOperation(value = "View info about free and busy workspaces")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved info"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @RequestMapping("/workspaces")
    public String greeting() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(workspaceRepository.getInfoAboutPlaces());
    }

    @GetMapping("/workspaces")
    public ResponseEntity<WorkspaceInfoDto> getWorkspaceForUuid(@RequestParam UUID workspaceUuid) throws ResourceNotFoundException {
        Workspace workspace = workspaceRepository.findByUuid(workspaceUuid);
        if (workspace == null) {
            throw new ResourceNotFoundException("Workspace not found for this uuid :: " + workspaceUuid);
        }
        List<WorkspaceRestriction> workspaceRestrictions = workspaceRestrictionRepository.findByWorkspaceUuid(workspaceUuid);
        WorkspaceInfoDto workspaceInfoDto = new WorkspaceInfoDto(
                workspace.getUuid(),
                workspace.getInternalId(),
                workspaceRestrictions.
                        stream().
                        map(WorkspaceRestriction::getType).
                        collect(Collectors.toList()),
                workspace.isBusy(),
                !workspace.isBusy());
        return ResponseEntity.ok(workspaceInfoDto);
    }

    @ApiOperation(value = "Set state of workspace")
    @PostMapping(path = "/workspaces", params = {"workspaceUuid", "confirm"})
        // Map ONLY POST Requests
    ResponseEntity<WorkspaceInfoDto> reserveSpace(@RequestParam UUID workspaceUuid, @RequestParam Boolean confirm, HttpServletResponse response) throws Exception {
        Workspace workspace = workspaceRepository.findByUuid(workspaceUuid);
        if (workspace == null) {
            throw new ResourceNotFoundException("Workspace not found for this uuid :: " + workspaceUuid);
        }
        if (!confirm) {
            throw new Exception("No confirm: " + workspaceUuid);
        }
        workspace.setBusy(!workspace.isBusy());
        return updateWorkspace(workspace, response);
    }

    @ApiOperation(value = "Set state of workspace")
    @PostMapping(path = "/workspaces", params = {"workspaceUuid", "pin", "confirm"})
        // Map ONLY POST Requests
    ResponseEntity<WorkspaceInfoDto> reserveRestrictedSpace(@RequestParam UUID workspaceUuid, @RequestParam String pin, @RequestParam Boolean confirm, HttpServletResponse response) throws Exception {
        Workspace workspace = workspaceRepository.findByUuid(workspaceUuid);
        if (workspace == null) {
            throw new ResourceNotFoundException("Workspace not found for this uuid: " + workspaceUuid);
        }
        List<WorkspaceRestriction> workspaceRestrictions = workspaceRestrictionRepository.findByWorkspaceUuid(workspaceUuid);
        if (confirm) {
            for (WorkspaceRestriction workspaceRestriction : workspaceRestrictions) {
                if (workspaceRestriction.getType() == WorkspaceType.PIN && !workspaceRestriction.getValue().equals(pin)) {
                    throw new ValidationFailedException("Wrong pin for this workspace: " + workspaceUuid);
                }
            }
        } else {
            throw new Exception("No confirm: " + workspaceUuid);
        }
        workspace.setBusy(!workspace.isBusy());
        return updateWorkspace(workspace, response);
    }

    private ResponseEntity<WorkspaceInfoDto> updateWorkspace(Workspace workspace, HttpServletResponse response) {
        final Workspace updatedWorkspace = workspaceRepository.save(workspace);
        producer.sendMessage(new WorkspaceStatus(workspace.getInternalId(), workspace.isBusy()));
        if (workspace != null) {
            ReservationLog reservationLog = ReservationLog
                    .builder()
                    .workspaceUuid(workspace.getUuid())
                    .build();
            reservationLogRepository.save(reservationLog);
            logger.info("Workspace {} was updated", workspace.getUuid().toString());
        }
//        final Cookie cookie = new Cookie("value", "test");
//        cookie.setSecure(true);
//        cookie.setHttpOnly(true);
//        cookie.setMaxAge(60 * 60 * 24);
//        response.addCookie(cookie);
        List<WorkspaceRestriction> workspaceRestrictions = workspaceRestrictionRepository.findByWorkspaceUuid(workspace.getUuid());
        WorkspaceInfoDto workspaceInfoDto = new WorkspaceInfoDto(
                workspace.getUuid(),
                workspace.getInternalId(),
                workspaceRestrictions.
                        stream().
                        map(WorkspaceRestriction::getType).
                        collect(Collectors.toList()),
                workspace.isBusy(),
                !workspace.isBusy());
        return ResponseEntity.ok(workspaceInfoDto);
    }
}
