package team.projectzebra.persistence.entity;

import io.swagger.annotations.Api;
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

    @ApiModelProperty(notes = "The workspaceMeta uuid")
    @OneToOne
    @JoinColumn(name = "workspace_uuid")
    private Workspace workspace;

    @ApiModelProperty(notes = "The workspaceMeta pin (if required)")
    @Column(name = "access_pin")
    private String accessPin;

    @ApiModelProperty(notes = "Optional row, if workspace is associated with projeсt.")
    @ManyToOne
    @JoinColumn(name = "project_uuid")
    Project project;
}
