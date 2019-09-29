package team.projectzebra.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BuildingDto {
    String companyUuid;
    String name;
    Integer capacity;
    String zip;
    String stateProvince;
    String city;
    String street;
    String number;
}
