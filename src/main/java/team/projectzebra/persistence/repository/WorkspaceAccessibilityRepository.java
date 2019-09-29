package team.projectzebra.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.projectzebra.persistence.entity.WorkspaceAccessibility;

import java.util.UUID;

public interface WorkspaceAccessibilityRepository extends JpaRepository<WorkspaceAccessibility, UUID> {
}
