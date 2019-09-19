package team.projectzebra.dto;

/**
 * Proudly created by dmaslov on 8/30/19.
 */
public class WorkspaceStatusDto {
    private String internalId;
    private Boolean busy;

    public WorkspaceStatusDto(String internalId, Boolean busy) {
        this.internalId = internalId;
        this.busy = busy;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public Boolean getBusy() {
        return busy;
    }

    public void setBusy(Boolean busy) {
        this.busy = busy;
    }
}
