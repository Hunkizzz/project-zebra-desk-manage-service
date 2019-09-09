package team.projectzebra.dao;

import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ReservationLogDao {
    private UUID workspaceUuid;

    public ReservationLogDao(UUID workspaceUUID) {
        this.workspaceUuid = workspaceUUID;
    }
}

