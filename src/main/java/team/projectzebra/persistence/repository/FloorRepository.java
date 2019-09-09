package team.projectzebra.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import team.projectzebra.persistence.entity.Floor;

import java.util.UUID;

/**
 * Proudly created by dgonyak on 09/09/2019.
 */
public interface FloorRepository extends CrudRepository<Floor, UUID> {
}
