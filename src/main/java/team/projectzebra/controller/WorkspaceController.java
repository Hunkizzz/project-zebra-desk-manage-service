package team.projectzebra.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.bridge.*;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.svg.SVGDocument;
import org.xml.sax.SAXException;
import team.projectzebra.dto.*;
import team.projectzebra.enums.WorkspaceStatus;
import team.projectzebra.enums.WorkspaceType;
import team.projectzebra.persistence.entity.*;
import team.projectzebra.persistence.repository.*;
import team.projectzebra.util.exceptions.ResourceNotFoundException;
import team.projectzebra.util.exceptions.ValidationFailedException;

@Api(value = "Workspaces Booking Management System")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/projectzebrateam-workspace-reservation-service")
public class WorkspaceController {
    WorkspaceRepository workspaceRepository;
    CompanyRepository companyRepository;
    ReservationLogRepository reservationLogRepository;
    WorkspaceRestrictionRepository workspaceRestrictionRepository;
    BuildingRepository buildingRepository;
    FloorRepository floorRepository;
    WorkspaceAccessibilityRepository workspaceAccessibilityRepository;
    WorkspaceEquipmentRepository workspaceEquipmentRepository;
//    Producer producer;

    private static final Logger logger = LoggerFactory.getLogger(WorkspaceController.class);

    @Autowired
    public WorkspaceController(WorkspaceRepository workspaceRepository,
                               CompanyRepository companyRepository,
                               ReservationLogRepository reservationLogRepository,
                               WorkspaceRestrictionRepository workspaceRestrictionRepository,
                               BuildingRepository buildingRepository,
                               FloorRepository floorRepository,
                               WorkspaceEquipmentRepository workspaceEquipmentRepository,
                               WorkspaceAccessibilityRepository workspaceAccessibilityRepository
//                               Producer producer
    ) {
        this.workspaceRepository = workspaceRepository;
        this.companyRepository = companyRepository;
        this.reservationLogRepository = reservationLogRepository;
        this.workspaceRestrictionRepository = workspaceRestrictionRepository;
        this.buildingRepository = buildingRepository;
        this.floorRepository = floorRepository;
        this.workspaceAccessibilityRepository = workspaceAccessibilityRepository;
        this.workspaceEquipmentRepository = workspaceEquipmentRepository;
//        this.producer = producer;
    }

    @ApiOperation(value = "View info about free and busy workspaces")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved info"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @RequestMapping(path = "/workspaces", params = {})
    public ResponseEntity<WorkspaceSummaryInfoDto> getSummaryInfo() throws JsonProcessingException {
        WorkspaceSummaryInfoDto workspaceSummaryInfoDto = new WorkspaceSummaryInfoDto();
        workspaceRepository.getInfoAboutPlaces().forEach(option -> {
            switch (option.getOption()) {
                case "Total":
                    workspaceSummaryInfoDto.setTotal(option.getCount());
                    break;
                case "Busy":
                    workspaceSummaryInfoDto.setBusy(option.getCount());
                    break;
                case "MGR":
                    workspaceSummaryInfoDto.setManager(option.getCount());
                    break;
//                case "Equipped":
//                    workspaceSummaryInfoDto.setEquipped(option.getCount());
//                    break;
                case "Free":
                    workspaceSummaryInfoDto.setFree(option.getCount());
                    break;
                default:
                    break;
            }
        });
        return ResponseEntity.ok(workspaceSummaryInfoDto);
    }

    @GetMapping(path = "/workspaces", params = {"workspaceUuid"})
    public ResponseEntity<WorkspaceInfoDto> getWorkspaceForUuid(@RequestParam UUID workspaceUuid,
                                                                HttpServletResponse response) throws ResourceNotFoundException {
        Workspace workspace = workspaceRepository.findByUuid(workspaceUuid);
        if (workspace == null) {
            throw new ResourceNotFoundException("Workspace not found for this uuid :: " + workspaceUuid);
        }
        if (workspace.getWorkspaceStatus() == WorkspaceStatus.FREE) {
            List<WorkspaceRestriction> workspaceRestrictions =
                    workspaceRestrictionRepository.findByWorkspaceUuid(workspaceUuid);
            WorkspaceInfoDto workspaceInfoDto = new WorkspaceInfoDto(
                    workspace.getUuid(),
                    workspace.getInternalId(),
                    workspaceRestrictions.
                            stream().
                            map(WorkspaceRestriction::getType).
                            collect(Collectors.toList()),
                    workspace.getWorkspaceStatus() == WorkspaceStatus.OCCUPIED,
                    workspace.getWorkspaceStatus() == WorkspaceStatus.FREE);
            return ResponseEntity.ok(workspaceInfoDto);
        } else {
            workspace.setWorkspaceStatus(WorkspaceStatus.FREE);
            return updateWorkspace(workspace, response);
        }
    }

    @ApiOperation(value = "Set state of workspace")
    @PostMapping(path = "/workspaces", params = {"workspaceUuid", "confirm"})
        // Map ONLY POST Requests
    ResponseEntity<WorkspaceInfoDto> reserveSpace(@RequestParam UUID workspaceUuid, @RequestParam Boolean confirm,
                                                  HttpServletResponse response) throws Exception {
        Workspace workspace = workspaceRepository.findByUuid(workspaceUuid);
        if (workspace == null) {
            throw new ResourceNotFoundException("Workspace not found for this uuid :: " + workspaceUuid);
        }
        if (!confirm) {
            throw new Exception("No confirm: " + workspaceUuid);
        }
        workspace.setWorkspaceStatus(WorkspaceStatus.OCCUPIED);
        return updateWorkspace(workspace, response);
    }

    @ApiOperation(value = "Set state of workspace")
    @PostMapping(path = "/workspaces", params = {"workspaceUuid", "pin", "confirm"})
        // Map ONLY POST Requests
    ResponseEntity<WorkspaceInfoDto> reserveRestrictedSpace(@RequestParam UUID workspaceUuid,
                                                            @RequestParam String pin, @RequestParam Boolean confirm,
                                                            HttpServletResponse response) throws Exception {
        Workspace workspace = workspaceRepository.findByUuid(workspaceUuid);
        if (workspace == null) {
            throw new ResourceNotFoundException("Workspace not found for this uuid: " + workspaceUuid);
        }
        List<WorkspaceRestriction> workspaceRestrictions =
                workspaceRestrictionRepository.findByWorkspaceUuid(workspaceUuid);
        if (confirm) {
            for (WorkspaceRestriction workspaceRestriction : workspaceRestrictions) {
                if (workspaceRestriction.getType() == WorkspaceType.PIN && !workspaceRestriction.getValue().equals(pin)) {
                    throw new ValidationFailedException("Wrong pin for this workspace: " + workspaceUuid);
                }
            }
        } else {
            throw new Exception("No confirm: " + workspaceUuid);
        }
        workspace.setWorkspaceStatus(WorkspaceStatus.OCCUPIED);
        return updateWorkspace(workspace, response);
    }


    //Block for add info in db
    @ApiOperation(value = "Add company")
    @PostMapping(path = "/add-company", params = {"name"})
    // Map ONLY POST Requests
    ResponseEntity<Company> addCompany(@RequestParam String name,
                                       HttpServletResponse response) throws Exception {
        Company company = Company.builder().name(name).build();
        companyRepository.save(company);
        return ResponseEntity.ok(company);
    }

    @ApiOperation(value = "Add building")
    @PostMapping(path = "/add-building")
        // Map ONLY POST Requests
    ResponseEntity<Building> addBuilding(@RequestBody BuildingDto buildingDto,
                                         HttpServletResponse response) throws Exception {
        Company company = companyRepository.findById((UUID.fromString(buildingDto.getCompanyUuid()))).get();
        Building building = Building.builder().company(company).
                name(buildingDto.getName()).
                capacity(buildingDto.getCapacity()).
                zip(buildingDto.getZip()).
                stateProvince(buildingDto.getStateProvince()).
                city(buildingDto.getCity()).
                street(buildingDto.getStreet()).
                number(buildingDto.getNumber()).build();

        buildingRepository.save(building);
        return ResponseEntity.ok(building);
    }

    @ApiOperation(value = "Add floor")
    @PostMapping(path = "/add-floor")
        // Map ONLY POST Requests
    ResponseEntity<Floor> addFloor(@RequestBody FloorDto floorDto,
                                   HttpServletResponse response) throws Exception {
        Building building = buildingRepository.findById((UUID.fromString(floorDto.getBuildingUuid()))).get();
        Floor floor = Floor.builder().building(building).
                name(floorDto.getName()).
                commonName(floorDto.getCommonName()).build();

        floorRepository.save(floor);
        return ResponseEntity.ok(floor);
    }

    @ApiOperation(value = "Add workspace")
    @PostMapping(path = "/add-workspace")
        // Map ONLY POST Requests
    ResponseEntity<String> addWorkspace(@RequestBody List<WorkspaceDto> workspaceDtos,
                                        HttpServletResponse response) throws Exception {
        workspaceDtos.forEach(workspaceDto -> {
            Floor floor = floorRepository.findById(UUID.fromString(workspaceDto.getFloorUuid())).get();
            Workspace workspace = Workspace.builder().floor(floor).
                    internalId(workspaceDto.getInternalId()).
                    name(workspaceDto.getName()).
                    workspaceStatus(workspaceDto.getWorkspaceStatus()).build();
            workspaceRepository.save(workspace);
            workspaceDto.getWorkspaceRestrictionDtos().forEach(workspaceRestrictionDto -> {
                WorkspaceRestriction workspaceRestriction = WorkspaceRestriction.builder().workspace(workspace).
                        type(workspaceRestrictionDto.getType()).
                        value(workspaceRestrictionDto.getValue()).build();
                workspaceRestrictionRepository.save(workspaceRestriction);
            });

            workspaceDto.getWorkspaceEquipmentDtos().forEach(workspaceEquipmentDto -> {
                WorkspaceEquipment workspaceEquipment = WorkspaceEquipment.builder().workspace(workspace).
                        type(workspaceEquipmentDto.getType()).
                        value(workspaceEquipmentDto.getValue()).build();
                workspaceEquipmentRepository.save(workspaceEquipment);
            });

            workspaceDto.getWorkspaceAccessibilityDtos().forEach(workspaceAccessibilityDto -> {
                WorkspaceAccessibility workspaceAccessibility = WorkspaceAccessibility.builder().workspace(workspace).
                        accessible(workspaceAccessibilityDto.getAccessible()).build();
                workspaceAccessibilityRepository.save(workspaceAccessibility);
            });

        });

        return ResponseEntity.ok("Okey");
    }


    //End

    //Work with SVG
    @GetMapping(path = "/test-image")
    public void testImage() throws IOException, ParserConfigurationException, SAXException {
        Map<String, GraphicsNode> svgCache = new HashMap<String, GraphicsNode>();

        String fileName = "/test.svg";
        URL resource = WorkspaceController.class.getResource(fileName);
        String test = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(String.valueOf(resource)).toString();
        GraphicsNode found;
        try {
            String xmlParser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory df = new SAXSVGDocumentFactory(xmlParser);
            SVGDocument doc = df.createSVGDocument(resource.toString());
            UserAgent userAgent = new UserAgentAdapter();
            DocumentLoader loader = new DocumentLoader(userAgent);
            BridgeContext ctx = new BridgeContext(userAgent, loader);
            ctx.setDynamicState(BridgeContext.DYNAMIC);
            GVTBuilder builder = new GVTBuilder();
            found = builder.build(ctx, doc);
            System.out.println("spdgjosdighosg");
        } catch (Exception e) {
            System.err.println("getSVGImage error: " + fileName);
            e.printStackTrace(System.err);
        }
    }

    private ResponseEntity<WorkspaceInfoDto> updateWorkspace(Workspace workspace, HttpServletResponse response) {
        final Workspace updatedWorkspace = workspaceRepository.save(workspace);
//        producer.sendMessage(new WorkspaceStatusDto(workspace.getInternalId(),
//                workspace.getWorkspaceStatus() == WorkspaceStatus.OCCUPIED));
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
        List<WorkspaceRestriction> workspaceRestrictions =
                workspaceRestrictionRepository.findByWorkspaceUuid(workspace.getUuid());
        WorkspaceInfoDto workspaceInfoDto = new WorkspaceInfoDto(
                workspace.getUuid(),
                workspace.getInternalId(),
                workspaceRestrictions.
                        stream().
                        map(WorkspaceRestriction::getType).
                        collect(Collectors.toList()),
                workspace.getWorkspaceStatus() == WorkspaceStatus.OCCUPIED,
                workspace.getWorkspaceStatus() == WorkspaceStatus.FREE);
        return ResponseEntity.ok(workspaceInfoDto);
    }
}
