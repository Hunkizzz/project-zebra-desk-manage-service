package team.projectzebra.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.projectzebra.dao.ReservationLogDao;
import team.projectzebra.dto.WorkspaceInfoDto;
import team.projectzebra.enums.WorkspaceType;
import team.projectzebra.persistence.entity.ReservationLog;
import team.projectzebra.persistence.entity.Workspace;
import team.projectzebra.persistence.repository.CompanyRepository;
import team.projectzebra.persistence.repository.ReservationLogRepository;
import team.projectzebra.persistence.repository.WorkspaceMetaRepository;
import team.projectzebra.persistence.repository.WorkspaceRepository;
import team.projectzebra.rabbitmq.Producer;
import team.projectzebra.util.exceptions.ResourceNotFoundException;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/projectzebrateam-workspace-reservation-service")
public class WorkspaceController {
    WorkspaceRepository workspaceRepository;
    CompanyRepository companyRepository;
    WorkspaceMetaRepository workspaceMetaRepository;
    ReservationLogRepository reservationLogRepository;
    Producer producer;

    private static final Logger logger = LoggerFactory.getLogger(WorkspaceController.class);

    @Autowired
    public WorkspaceController(WorkspaceRepository workspaceRepository,
                               CompanyRepository companyRepository,
                               WorkspaceMetaRepository workspaceMetaRepository,
                               ReservationLogRepository reservationLogRepository,
                               Producer producer) {
        this.workspaceRepository = workspaceRepository;
        this.companyRepository = companyRepository;
        this.workspaceMetaRepository = workspaceMetaRepository;
        this.reservationLogRepository = reservationLogRepository;
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
        return objectMapper.writeValueAsString(workspaceRepository.getInfoAboutWorkspaces());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/workspaces")
    public ResponseEntity<WorkspaceInfoDto> getWorkspaceForUuid(@RequestParam UUID workspaceUuid) throws ResourceNotFoundException {
        Workspace workspace = workspaceRepository.findByUuid(workspaceUuid);
        if (workspace == null) {
            throw new ResourceNotFoundException("Workspace not found for this uuid :: " + workspaceUuid);
        }

        WorkspaceInfoDto workspaceInfoDto = new WorkspaceInfoDto(
                workspace.getUuid(),
                workspace.getInternalId(),
                new String[]{
                        workspace.getWorkspaceMeta().getType().toString(),
                        workspace.getWorkspaceMeta().getAccessPin()
                },
                workspace.isBusy(),
                !workspace.isBusy());
        return ResponseEntity.ok(workspaceInfoDto);
    }

    @ApiOperation(value = "Set state of workspace")
    @PostMapping(path = "/workspaces", params = {"workspaceUuid"})
        // Map ONLY POST Requests
    ResponseEntity<Workspace> reserveSpace(@RequestParam UUID workspaceUuid, HttpServletResponse response) throws ResourceNotFoundException {
        Workspace workspace = workspaceRepository.findByUuid(workspaceUuid);
        if (workspace == null) {
            throw new ResourceNotFoundException("Workspace not found for this uuid :: " + workspaceUuid);
        }
        workspace.setBusy(!workspace.isBusy());

        return updateWorkspace(workspace, response);
    }

    @ApiOperation(value = "Set state of workspace")
    @PostMapping(path = "/workspaces", params = {"workspaceUuid", "restricted"})
        // Map ONLY POST Requests
    ResponseEntity<Workspace> reserveRestrictedSpace(@RequestParam UUID workspaceUuid, @RequestParam boolean restricted, HttpServletResponse response) throws ResourceNotFoundException {
        Workspace workspace = workspaceRepository.findByUuid(workspaceUuid);
        if (workspace == null) {
            throw new ResourceNotFoundException("Workspace not found for this uuid :: " + workspaceUuid);
        }
        if (workspace.getWorkspaceMeta().getType() == WorkspaceType.RESTRICTED && restricted) {
            workspace.setBusy(!workspace.isBusy());
        }

        return updateWorkspace(workspace, response);
    }

    @ApiOperation(value = "Set state of workspace")
    @PostMapping(path = "/workspaces", params = {"workspaceUuid", "pin"})
        // Map ONLY POST Requests
    ResponseEntity<Workspace> reserveRestrictedSpace(@RequestParam UUID workspaceUuid, @RequestParam String pin, HttpServletResponse response) throws ResourceNotFoundException {
        Workspace workspace = workspaceRepository.findByUuid(workspaceUuid);
        if (workspace == null) {
            throw new ResourceNotFoundException("Workspace not found for this uuid :: " + workspaceUuid);
        }
        if (workspace.getWorkspaceMeta().getType() == WorkspaceType.RESTRICTED && workspace.getWorkspaceMeta().getAccessPin().equals(pin)) {
            workspace.setBusy(!workspace.isBusy());
        }
        return updateWorkspace(workspace, response);
    }

    private ResponseEntity<Workspace> updateWorkspace(Workspace workspace, HttpServletResponse response) {
        final Workspace updatedWorkspace = workspaceRepository.save(workspace);

        ReservationLogDao reservationLogDao = workspaceRepository.getInfoForReservationLog(workspace.getUuid());

        if (reservationLogDao != null) {
            ReservationLog reservationLog = ReservationLog.builder()
                    .companyBuildingUuid(reservationLogDao.getBuildingCompany().getUuid())
                    .workspaceUuid(reservationLogDao.getWorkspaceUuid()).build();
            reservationLogRepository.save(reservationLog);
            logger.info("Workspace {} was updated", workspace.getUuid().toString());
        }
//        final Cookie cookie = new Cookie("value", "test");
//        cookie.setSecure(true);
//        cookie.setHttpOnly(true);
//        cookie.setMaxAge(60 * 60 * 24);
//        response.addCookie(cookie);
        return ResponseEntity.ok(updatedWorkspace);
    }
}
