package team.projectzebra.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceSummaryInfoDto {
    private int total;
    private int manager;
    private int equipped;
    private int busy;
    private int free;
}
