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
@Table(name = "building")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Building {
    @ApiModelProperty(notes = "The java generated UUID of building")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    @ApiModelProperty(notes = "The company")
    @OneToOne
    @JoinColumn(name = "company")
    private Company company;
    @ApiModelProperty(notes = "The building name")
    private String name;
    @ApiModelProperty(notes = "The building capacity")
    private Integer capacity;
    @ApiModelProperty(notes = "The building zipcode")
    private String zip;
    @ApiModelProperty(notes = "The building state province")
    private String stateProvince;
    @ApiModelProperty(notes = "The building city")
    private String city;
    @ApiModelProperty(notes = "The building street")
    private String street;
    @ApiModelProperty(notes = "The building number")
    private String number;
}
