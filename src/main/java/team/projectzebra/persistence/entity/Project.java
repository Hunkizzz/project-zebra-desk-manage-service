package team.projectzebra.persistence.entity;

import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Proudly created by dgonyak on 02/09/2019.
 */
@Data
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
public class Project {
    @ApiModelProperty(notes = "The Java generated project uuid")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    @ApiModelProperty(notes = "The project name")
    private String name;
}

