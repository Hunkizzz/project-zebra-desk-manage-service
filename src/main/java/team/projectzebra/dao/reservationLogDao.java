package team.projectzebra.dao;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import team.projectzebra.persistence.entity.BuildingCompany;

@Getter
@Setter
public class reservationLogDao {
   private UUID workspaceUUID;
   private BuildingCompany buildingCompany;

   public reservationLogDao(UUID workspaceUUID, BuildingCompany buildingCompany){
       this.buildingCompany = buildingCompany;
       this.workspaceUUID = workspaceUUID;
   }
}
