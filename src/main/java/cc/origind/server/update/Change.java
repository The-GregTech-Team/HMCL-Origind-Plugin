package cc.origind.server.update;

import com.google.gson.annotations.SerializedName;

public class Change {
    @SerializedName("type")
    private String changeType;
    private String file;
    private String to;

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Change{" +
                "changeType=" + changeType +
                ", file='" + file + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
