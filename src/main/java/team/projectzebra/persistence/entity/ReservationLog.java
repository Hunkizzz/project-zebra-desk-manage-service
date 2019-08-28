package team.projectzebra.persistence.entity;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.*;

@Data
@Entity
@Builder(toBuilder = true)
@Table(name = "reservation_log")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ReservationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date createdAt;

    @Column(name = "company_building_uuid")
    private UUID companyBuildingUUID;

    @Column(name = "workspace_uuid")
    private UUID workspaceUUID;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
