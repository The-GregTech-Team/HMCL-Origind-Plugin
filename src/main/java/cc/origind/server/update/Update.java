package cc.origind.server.update;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Update {
    private String latestVersion;

    @SerializedName("versions")
    private Map<String, Version> versionMap = new HashMap<>();

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public Map<String, Version> getVersionMap() {
        return versionMap;
    }

    public void setVersionMap(Map<String, Version> versionMap) {
        this.versionMap = versionMap;
    }

    @Override
    public String toString() {
        return "Update{" +
                "latestVersion='" + latestVersion + '\'' +
                ", versionMap=" + versionMap +
                '}';
    }
}
