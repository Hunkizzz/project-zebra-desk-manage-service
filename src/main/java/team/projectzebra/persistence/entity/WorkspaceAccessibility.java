package team.projectzebra.persistence.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.projectzebra.enums.WorkspaceAccess;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceAccessibility {
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
    private WorkspaceAccess accessible;
}
