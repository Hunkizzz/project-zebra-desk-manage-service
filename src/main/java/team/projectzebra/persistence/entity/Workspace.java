package team.projectzebra.persistence.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import team.projectzebra.enums.WorkspaceState;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

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

    @ManyToOne
    @JoinColumn(name = "floor")
    private Floor floor;

    @ApiModelProperty(notes = "The workspace internal id")
    @Column(name = "internal_id")
    private String internalId;

    @ApiModelProperty(notes = "The workspace name")
    @NotNull
    private String name;

    @ApiModelProperty(notes = "The workspace status")
    @NotNull
    @Enumerated(EnumType.STRING)
    private WorkspaceState status;
}
