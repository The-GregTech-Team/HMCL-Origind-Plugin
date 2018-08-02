package cc.origind.server;

public class OrigindConfig {
    /**
     * 更新JSON的网址
     */
    private String updateJsonURL;

    /**
     * 下载前缀(通常为OSS网址)
     */
    private String downloadPrefix;

    /**
     * 当前服务器版本
     */
    private String version;

    public OrigindConfig() {
        downloadPrefix = "http://origind-download-1251214188.cossh.myqcloud.com/";
        updateJsonURL = downloadPrefix + "update.json";
        version = "20180802";
    }

    public String getUpdateJsonURL() {
        return updateJsonURL;
    }

    public void setUpdateJsonURL(String updateJsonURL) {
        this.updateJsonURL = updateJsonURL;
    }

    public String getDownloadPrefix() {
        return downloadPrefix;
    }

    public void setDownloadPrefix(String downloadPrefix) {
        this.downloadPrefix = downloadPrefix;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "OrigindConfig{" +
                "updateJsonURL='" + updateJsonURL + '\'' +
                ", downloadPrefix='" + downloadPrefix + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
