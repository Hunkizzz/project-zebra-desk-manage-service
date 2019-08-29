package team.projectzebra.persistence.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Builder(toBuilder = true)
@Table(name = "workspace_pin")
@AllArgsConstructor
@NoArgsConstructor
public class WorkspacePin {
    @ApiModelProperty(notes = "The Java generated WorkspacePin uuid")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ApiModelProperty(notes = "The WorkspacePin workspace_meta uuid")
    @OneToOne
    @JoinColumn(name = "workspace_uuid")
    WorkspaceMeta workspaceMeta;

    @ApiModelProperty(notes = "The workspacePin name")
    private String value;
}
