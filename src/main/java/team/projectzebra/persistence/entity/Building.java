package team.projectzebra.persistence.entity;

import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "building")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Building {
    @ApiModelProperty(notes = "The java generated UUID of building")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    @ApiModelProperty(notes = "The building name")
    private String name;
    @ApiModelProperty(notes = "The building capacity")
    private Integer capacity;
    @ApiModelProperty(notes = "The building city")
    private String city;
    @ApiModelProperty(notes = "The building street")
    private String street;
    @ApiModelProperty(notes = "The building number")
    private String building;
}
