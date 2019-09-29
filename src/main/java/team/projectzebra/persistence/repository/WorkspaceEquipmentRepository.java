package team.projectzebra.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.projectzebra.persistence.entity.WorkspaceEquipment;

import java.util.UUID;

public interface WorkspaceEquipmentRepository extends JpaRepository<WorkspaceEquipment, UUID> {
}
