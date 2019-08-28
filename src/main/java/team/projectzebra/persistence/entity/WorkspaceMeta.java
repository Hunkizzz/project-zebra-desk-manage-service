package team.projectzebra.persistence.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.*;
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

    @ManyToOne
    @JoinColumn(name = "building_company_uuid")
    BuildingCompany buildingCompany;

    private int floor;

    private String name;

    @Column(name = "type", length = 25)
    @NotNull
    @Enumerated(EnumType.STRING)
    private WorkspaceType type;

    @OneToOne
    @JoinColumn(name="require_pin")
    private WorkspacePin requirePin;

    @Column(name="project_uuid")
    private UUID projectUUID;

}
