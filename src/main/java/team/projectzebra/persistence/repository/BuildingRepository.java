package team.projectzebra.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import team.projectzebra.persistence.entity.Building;

import java.util.UUID;

public interface BuildingRepository extends CrudRepository<Building, UUID> {
}
