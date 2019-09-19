package team.projectzebra.persistence.entity;

import io.swagger.annotations.ApiModelProperty;
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
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.projectzebra.enums.WorkspaceStatus;

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
    private WorkspaceStatus workspaceStatus;
}
