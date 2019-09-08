package team.projectzebra.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class WorkspaceInfoDto {
    private UUID workspaceUuid;
    private String internalId;
    private String[] restrictions;
    private boolean workspaceReserved;
    private boolean workspaceFree;
}
