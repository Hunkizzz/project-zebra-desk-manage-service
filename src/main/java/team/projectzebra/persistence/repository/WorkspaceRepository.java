package team.projectzebra.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team.projectzebra.dto.WorkspaceDto;
import team.projectzebra.persistence.entity.Workspace;

import java.util.List;
import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
    Workspace findByUuid(UUID uuid);

    @Query(value = "SELECT 'Free' as state, count(busy) as count FROM workspace w where busy = false\n" +
            "            UNION ALL\n" +
            "            \n" +
            "            SELECT 'Busy' as state, count(busy) FROM workspace where busy = true\n" +
            "            UNION ALL\n" +
            "            Select 'Total' as state, count(*) FROM workspace\n" +
            "UNION ALL\n" +
            "\tSELECT 'MGR' as state, count(*) FROM workspace w LEFT JOIN workspace_restriction  wr on w.uuid = wr.workspace_uuid where wr.type = 'MGR'\n" +
            "\t\n" +
            "\tUNION ALL\n" +
            "\tSELECT 'Equipped' as state, count(*) FROM workspace w where w.equipped = true;", nativeQuery = true)
    List<WorkspaceDto> getInfoAboutPlaces();

//    @Query(value = "SELECT new team.projectzebra.dao.ReservationLogDao(w.uuid, wm.buildingCompany) from Workspace w left join Floor f on wm.uuid = workspace_meta_uuid where w.uuid = :uuid")
//    ReservationLogDao getInfoForReservationLog(@Param("uuid") UUID uuid);

    @Modifying
    @Transactional
    @Query(value = "update Workspace set busy = case when busy = true then false else true end where uuid = :uuid")
    int setStatusForWorkspace(@Param("uuid") UUID uuid);
}
