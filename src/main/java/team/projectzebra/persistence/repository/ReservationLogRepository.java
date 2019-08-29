package team.projectzebra.persistence.repository;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import team.projectzebra.persistence.entity.ReservationLog;

public interface ReservationLogRepository extends CrudRepository<ReservationLog, UUID> {
}
