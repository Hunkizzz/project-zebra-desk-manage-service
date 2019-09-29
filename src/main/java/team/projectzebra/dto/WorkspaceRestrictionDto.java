package team.projectzebra.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.projectzebra.enums.WorkspaceType;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceRestrictionDto {
    private WorkspaceType type;
    private String value;
}
