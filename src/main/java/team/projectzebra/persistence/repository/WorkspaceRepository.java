package team.projectzebra.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team.projectzebra.dto.InfoDto;
import team.projectzebra.persistence.entity.Workspace;

import java.util.List;
import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
    Workspace findByUuid(UUID uuid);

    @Query(value = "SELECT 'Free' as option, count(1) as count FROM workspace w where workspace_status = 'FREE'\n" +
            "                        UNION ALL\n" +
            "                        SELECT 'Busy' as option, count(1) FROM workspace where workspace_status = 'OCCUPIED'\n" +
            "                        UNION ALL\n" +
            "                       Select 'Total' as option, count(*) FROM workspace\n" +
            "            UNION ALL\n" +
            "SELECT 'MGR' as option, count(*) FROM workspace w LEFT JOIN workspace_restriction  wr on w.uuid = wr.workspace_uuid where wr.type = 'MGR'", nativeQuery = true)
    List<InfoDto> getInfoAboutPlaces();

//    @Query(value = "SELECT new team.projectzebra.dao.ReservationLogDao(w.uuid, wm.buildingCompany) from Workspace w left join Floor f on wm.uuid = workspace_meta_uuid where w.uuid = :uuid")
//    ReservationLogDao getInfoForReservationLog(@Param("uuid") UUID uuid);

//    @Modifying
//    @Transactional
//    @Query(value = "update Workspace set busy = case when busy = true then false else true end where uuid = :uuid")
//    int setStatusForWorkspace(@Param("uuid") UUID uuid);
}
