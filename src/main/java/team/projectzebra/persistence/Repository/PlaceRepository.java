package team.projectzebra.persistence.Repository;

import org.springframework.data.repository.CrudRepository;
import team.projectzebra.persistence.entity.Place;

public interface PlaceRepository extends CrudRepository<Place,Integer> {
}
