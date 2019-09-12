package team.projectzebra.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import team.projectzebra.enums.WorkspaceType;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class WorkspaceInfoDto {
    private UUID workspaceUuid;
    private String workspaceInternalId;
    private List<WorkspaceType> restrictions;
    private boolean workspaceReserved;
    private boolean workspaceFree;
}