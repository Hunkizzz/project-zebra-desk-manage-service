package team.projectzebra.persistence.repository;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import team.projectzebra.persistence.entity.WorkspaceMeta;

public interface WorkspaceMetaRepository extends CrudRepository<WorkspaceMeta,Integer> {
    WorkspaceMeta findOneByUuid(UUID uuid);
}
