package cc.origind.server.update;

import java.util.ArrayList;
import java.util.List;

public class Version {
    private String releaseTime;
    private List<Change> changes = new ArrayList<>();

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public List<Change> getChanges() {
        return changes;
    }

    public void setChanges(List<Change> changes) {
        this.changes = changes;
    }

    @Override
    public String toString() {
        return "Version{" +
                "releaseTime='" + releaseTime + '\'' +
                ", changes=" + changes +
                '}';
    }
}
