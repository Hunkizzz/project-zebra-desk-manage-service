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
import team.projectzebra.dao.ReservationLogDAO;
import team.projectzebra.persistence.entity.ReservationLog;
import team.projectzebra.persistence.entity.Workspace;
import team.projectzebra.persistence.repository.CompanyRepository;
import team.projectzebra.persistence.repository.ReservationLogRepository;
import team.projectzebra.persistence.repository.WorkspaceMetaRepository;
import team.projectzebra.persistence.repository.WorkspaceRepository;
import team.projectzebra.util.exceptions.ResourceNotFoundException;

@RestController
@RequestMapping("/api/v1/projectzebrateam-workplace-reservation-service")
public class WorkSpaceController {
    WorkspaceRepository workspaceRepository;
    CompanyRepository companyRepository;
    WorkspaceMetaRepository workspaceMetaRepository;
    ReservationLogRepository reservationLogRepository;

    private static final Logger logger = LoggerFactory.getLogger(WorkSpaceController.class);

    @Autowired
    public WorkSpaceController(WorkspaceRepository workspaceRepository,
                               CompanyRepository companyRepository,
                               WorkspaceMetaRepository workspaceMetaRepository,
                               ReservationLogRepository reservationLogRepository) {
        this.workspaceRepository = workspaceRepository;
        this.companyRepository = companyRepository;
        this.workspaceMetaRepository = workspaceMetaRepository;
        this.reservationLogRepository = reservationLogRepository;
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

        ReservationLogDAO reservationLogDAO = workspaceRepository.getInfoForReservationLog(workspaceUUID);

        if (reservationLogDAO != null) {
            ReservationLog reservationLog = ReservationLog.builder()
                    .companyBuildingUUID(reservationLogDAO.getBuildingCompany().getUuid())
                    .workspaceUUID(reservationLogDAO.getWorkspaceUUID()).build();
            reservationLogRepository.save(reservationLog);
            logger.info("Workspace {} was updated", workspaceUUID.toString());
        }
        return ResponseEntity.ok(updatedWorkspace);
    }
}
