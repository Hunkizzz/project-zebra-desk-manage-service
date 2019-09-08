package team.projectzebra.dao;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import team.projectzebra.persistence.entity.BuildingCompany;

@Getter
@Setter
public class ReservationLogDao {
   private UUID workspaceUuid;
   private BuildingCompany buildingCompany;

   public ReservationLogDao(UUID workspaceUuid, BuildingCompany buildingCompany){
       this.buildingCompany = buildingCompany;
       this.workspaceUuid = workspaceUuid;
   }
}
