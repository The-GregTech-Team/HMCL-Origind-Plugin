package cc.origind.server.update;

import com.google.gson.annotations.SerializedName;

public class Change {
    @SerializedName("type")
    private String changeType;
    private String file;
    private String to;

    public Change(String changeType, String file) {
        this.changeType = changeType;
        this.file = file;
    }

    public Change(String changeType, String file, String to) {
        this.changeType = changeType;
        this.file = file;
        this.to = to;
    }

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

    public boolean isUpgrade() {
        return changeType.equals("upgrade");
    }

    public boolean isAdd() {
        return changeType.equals("add");
    }

    public boolean isDelete() {
        return changeType.equals("delete");
    }

    public boolean isMerged() {
        return changeType.equals("merged");
    }

    public String getDestination() {
        return isUpgrade() ? getTo() : getFile();
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
