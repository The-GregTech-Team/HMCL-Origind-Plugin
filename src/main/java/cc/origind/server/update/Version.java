package cc.origind.server.update;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Version {
    private String id;
    private String releaseTime;
    private List<Change> changes = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Version)) return false;
        Version version = (Version) o;
        return Objects.equals(getId(), version.getId()) &&
                Objects.equals(getReleaseTime(), version.getReleaseTime()) &&
                Objects.equals(getChanges(), version.getChanges());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getReleaseTime(), getChanges());
    }

    @Override
    public String toString() {
        return "Version{" +
                "id='" + id + '\'' +
                ", releaseTime='" + releaseTime + '\'' +
                ", changes=" + changes +
                '}';
    }
}
