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
@Table(name = "floor")
@AllArgsConstructor
@NoArgsConstructor
public class Floor {
    @ApiModelProperty(notes = "The Java generated floor uuid")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ApiModelProperty(notes = "The floor")
    private int floor;

    @ApiModelProperty(notes = "Optional row, if workspace is associated with projeсt.")
    @ManyToOne
    @JoinColumn(name = "building")
    Building building;
}
