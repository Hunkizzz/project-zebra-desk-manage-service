package team.projectzebra.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import team.projectzebra.persistence.entity.Workplace;

public interface WorkplaceRepository extends CrudRepository<Workplace,Integer> {
}
