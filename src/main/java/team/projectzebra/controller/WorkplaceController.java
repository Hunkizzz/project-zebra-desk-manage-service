package team.projectzebra.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.projectzebra.persistence.entity.Workplace;
import team.projectzebra.persistence.repository.WorkplaceRepository;

@RestController
@RequestMapping("/api/v1/projectzebrateam-workplace-reservation-service")
public class WorkplaceController {
    private WorkplaceRepository workplaceRepository;

    @Autowired
    public WorkplaceController(WorkplaceRepository workplaceRepository) {
        this.workplaceRepository = workplaceRepository;
    }

    @RequestMapping("/workplaces")
    public String greeting() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(workplaceRepository.findAll());
    }

    @PostMapping(path = "/workplaces")
        // Map ONLY POST Requests
    ResponseEntity addNewPlace(@RequestParam String location
            , @RequestParam boolean isBusy) {

        Workplace workplace = new Workplace(location, isBusy);
        System.out.println("Saving workplace");
        workplaceRepository.save(workplace);
        return new ResponseEntity(HttpStatus.OK);
    }
}
