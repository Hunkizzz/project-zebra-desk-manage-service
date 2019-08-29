package team.projectzebra.persistence.entity;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;
import team.projectzebra.enums.WorkspaceType;

@Data
@Entity
@Builder(toBuilder = true)
@Table(name = "workspace_meta")
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceMeta {
    @ApiModelProperty(notes = "The Java generated workspaceMeta uuid")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ApiModelProperty(notes = "The workspaceMeta relation between building and company uuid")
    @ManyToOne
    @JoinColumn(name = "building_company_uuid")
    BuildingCompany buildingCompany;

    @ApiModelProperty(notes = "The workspaceMeta floor")
    private int floor;

    @ApiModelProperty(notes = "The workspaceMeta name")
    private String name;

    @ApiModelProperty(notes = "The workspaceMeta type")
    @Column(name = "type", length = 25)
    @NotNull
    @Enumerated(EnumType.STRING)
    private WorkspaceType type;

    @ApiModelProperty(notes = "The workspaceMeta relation pin (if required)")
    @OneToOne
    @JoinColumn(name="require_pin")
    private WorkspacePin requirePin;

    @ApiModelProperty(notes = "The workspaceMeta project uuid (if required)")
    @Column(name="project_uuid")
    private UUID projectUUID;

}
