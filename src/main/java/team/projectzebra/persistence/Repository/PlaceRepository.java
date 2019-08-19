package ru.deloitte.persistence.Repository;

import org.springframework.data.repository.CrudRepository;
import ru.deloitte.persistence.entity.Place;

public interface PlaceRepository extends CrudRepository<Place,Integer> {
}
