package team.projectzebra.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import team.projectzebra.persistence.entity.Building;

public interface BuildingRepository extends CrudRepository<Building,Integer> {
}
