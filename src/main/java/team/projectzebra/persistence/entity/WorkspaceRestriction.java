package team.projectzebra.persistence.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.projectzebra.enums.WorkspaceType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Proudly created by dgonyak on 02/09/2019.
 */
@Entity
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceRestriction {
    @ApiModelProperty(notes = "The Java generated workspace restriction uuid")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "workspace_uuid")
    private Workspace workspace;

    @ApiModelProperty(notes = "The workspace restriction type")
    @Column(name = "type", length = 25)
    @NotNull
    @Enumerated(EnumType.STRING)
    private WorkspaceType type;

    @ApiModelProperty(notes = "The workspace restriction value of pin")
    private String value;
}
