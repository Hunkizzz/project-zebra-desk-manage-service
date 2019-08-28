package team.projectzebra.persistence.entity;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.*;

@Data
@Entity
@Table(name = "building_company")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BuildingCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @OneToOne
    @JoinColumn(name="company_uuid")
    Company company;

    @OneToOne
    @JoinColumn(name="building_uuid")
    Building building;
}
