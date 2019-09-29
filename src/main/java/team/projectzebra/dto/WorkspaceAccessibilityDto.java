package team.projectzebra.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.projectzebra.enums.WorkspaceAccess;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceAccessibilityDto {
    private WorkspaceAccess accessible;
}
