package team.projectzebra.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.projectzebra.enums.WorkspaceStatus;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceDto {
    List<WorkspaceAccessibilityDto> workspaceAccessibilityDtos;
    List<WorkspaceEquipmentDto> WorkspaceEquipmentDtos;
    List<WorkspaceRestrictionDto> WorkspaceRestrictionDtos;
    String floorUuid;
    String internalId;
    String name;
    WorkspaceStatus workspaceStatus;
}
