package team.projectzebra.persistence.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;
import lombok.Setter;

@Data
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Workspace {
    @ApiModelProperty(notes = "The Java generated workspace uuid")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ApiModelProperty(notes = "The workspaceMeta uuid")
    @OneToOne
    @JoinColumn(name = "workspace_uuid")
    private WorkspaceMeta workspaceMeta;

    @ApiModelProperty(notes = "The workspace state")
    private boolean busy;
}
