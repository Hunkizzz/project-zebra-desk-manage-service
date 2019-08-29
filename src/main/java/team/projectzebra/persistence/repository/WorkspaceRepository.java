package team.projectzebra.persistence.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team.projectzebra.dao.ReservationLogDAO;
import team.projectzebra.dto.WorkspaceDTO;
import team.projectzebra.persistence.entity.Workspace;

public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
    Workspace findByUuid(UUID uuid);

    @Query(value = "SELECT 'Free' as state, count(busy) as count FROM workspace w where busy = false\n" +
            "\n" +
            "UNION ALL\n" +
            "\n" +
            "SELECT 'Busy' as state, count(busy) FROM workspace where busy = true\n" +
            "\n" +
            "UNION ALL \n" +
            "Select 'Total' as state, count(*) FROM workspace;", nativeQuery = true)
    List<WorkspaceDTO> getInfoAboutPlaces();

    @Query(value = "SELECT new team.projectzebra.dao.ReservationLogDAO(w.uuid, wm.buildingCompany) from Workspace w left join WorkspaceMeta wm on wm.uuid = workspace_meta_uuid where w.uuid = :uuid")
    ReservationLogDAO getInfoForReservationLog(@Param("uuid") UUID uuid);

    @Modifying
    @Transactional
    @Query(value = "update Workspace set busy = case when busy = true then false else true end where uuid = :uuid")
    int setStatusForWorkspace(@Param("uuid") UUID uuid);
}
