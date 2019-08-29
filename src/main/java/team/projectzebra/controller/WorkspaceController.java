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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.projectzebra.dao.reservationLogDao;
import team.projectzebra.dto.WorkspaceStatus;
import team.projectzebra.persistence.entity.ReservationLog;
import team.projectzebra.persistence.entity.Workspace;
import team.projectzebra.persistence.repository.CompanyRepository;
import team.projectzebra.persistence.repository.ReservationLogRepository;
import team.projectzebra.persistence.repository.WorkspaceMetaRepository;
import team.projectzebra.persistence.repository.WorkspaceRepository;
import team.projectzebra.rabbitmq.Producer;
import team.projectzebra.util.exceptions.ResourceNotFoundException;

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

    @ApiOperation(value = "Set state of workspace")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/workspaces")
        // Map ONLY POST Requests
    ResponseEntity reserveSpace(@RequestParam UUID workspaceUUID) throws ResourceNotFoundException {
        Workspace workspace = workspaceRepository.findByUuid(workspaceUUID);
        if (workspace == null) {
            throw new ResourceNotFoundException("Workspace not found for this uuid :: " + workspaceUUID);
        }
        workspace.setBusy(!workspace.isBusy());

        final Workspace updatedWorkspace = workspaceRepository.save(workspace);
        producer.sendMessage(new WorkspaceStatus(workspace.getInternalId(), workspace.isBusy()));
        reservationLogDao reservationLogDao = workspaceRepository.getInfoForReservationLog(workspaceUUID);

        if (reservationLogDao != null) {
            ReservationLog reservationLog = ReservationLog.builder()
                    .companyBuildingUUID(reservationLogDao.getBuildingCompany().getUuid())
                    .workspaceUUID(reservationLogDao.getWorkspaceUUID()).build();
            reservationLogRepository.save(reservationLog);
            logger.info("Workspace {} was updated", workspaceUUID.toString());
        }
        return ResponseEntity.ok(updatedWorkspace);
    }
}
