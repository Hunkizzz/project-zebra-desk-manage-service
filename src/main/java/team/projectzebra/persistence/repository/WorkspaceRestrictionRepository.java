package team.projectzebra.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import team.projectzebra.persistence.entity.WorkspaceRestriction;

import java.util.List;
import java.util.UUID;

/**
 * Proudly created by dgonyak on 09/09/2019.
 */
public interface WorkspaceRestrictionRepository extends CrudRepository<WorkspaceRestriction, UUID> {
    List<WorkspaceRestriction> findByWorkspaceUuid(UUID workspaceUuid);
}
