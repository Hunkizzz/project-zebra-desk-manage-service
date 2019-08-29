package team.projectzebra.persistence.entity;

import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "building_company")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BuildingCompany {
    @ApiModelProperty(notes = "The java generated UUID of relation between company and building")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ApiModelProperty(notes = "The relation company uuid")
    @OneToOne
    @JoinColumn(name="company_uuid")
    Company company;

    @ApiModelProperty(notes = "The relation building uuid")
    @OneToOne
    @JoinColumn(name="building_uuid")
    Building building;
}
