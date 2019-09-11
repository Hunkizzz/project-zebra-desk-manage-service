package team.projectzebra.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import team.projectzebra.persistence.entity.ReservationLog;

import java.util.UUID;

public interface ReservationLogRepository extends CrudRepository<ReservationLog, UUID> {
}
