package team.projectzebra.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.projectzebra.persistence.Repository.PlaceRepository;
import team.projectzebra.persistence.entity.Place;

@RestController
@RequestMapping("/api/v1/place")
public class PlaceController {
    PlaceRepository placeRepo;
    @Autowired
    public PlaceController(PlaceRepository placeRepo){
        this.placeRepo = placeRepo;
    }

    @RequestMapping("/get-all")
    public String greeting() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(placeRepo.findAll());
    }

    @PostMapping(path="/add-new") // Map ONLY POST Requests
    void addNewPlace (@RequestParam String location
            , @RequestParam boolean isBusy) {

        Place place = new Place(location, isBusy);
        placeRepo.save(place);
    }
}
