package team.projectzebra.persistence.entity;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder(toBuilder = true)
@Table(name = "workspace_pin")
@AllArgsConstructor
@NoArgsConstructor
public class WorkspacePin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @OneToOne
    @JoinColumn(name = "workspace_uuid")
    WorkspaceMeta workspaceMeta;

    private String value;
}
