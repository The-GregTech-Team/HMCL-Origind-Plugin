package cc.origind.server.update;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Update implements Comparator<Version> {
    private String latestVersion;

    private List<Version> versions = new ArrayList<>();

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public List<Version> getVersions() {
        return versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    @Override
    public String toString() {
        return "Update{" +
                "latestVersion='" + latestVersion + '\'' +
                ", versions=" + versions +
                '}';
    }

    @Override
    public int compare(Version o1, Version o2) {
        return Integer.compare(versions.indexOf(o1), versions.indexOf(o2));
    }
}
