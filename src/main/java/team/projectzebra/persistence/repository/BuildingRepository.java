package team.projectzebra.persistence.repository;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import team.projectzebra.persistence.entity.Building;

public interface BuildingRepository extends CrudRepository<Building, UUID> {
}
