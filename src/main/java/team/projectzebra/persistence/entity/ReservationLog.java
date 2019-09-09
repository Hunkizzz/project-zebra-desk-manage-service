package team.projectzebra.persistence.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Builder(toBuilder = true)
@Table(name = "reservation_log")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ReservationLog {
    @ApiModelProperty(notes = "The Java generated reservationLog uuid")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ApiModelProperty(notes = "The workspace uuid")
    @Column(name = "workspace_uuid")
    private UUID workspaceUuid;

    @ApiModelProperty(notes = "The Java generated reservationLog date when row was created")
    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
