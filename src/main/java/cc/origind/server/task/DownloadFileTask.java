package cc.origind.server.task;

import cc.origind.server.OrigindPlugin;
import org.jackhuang.hmcl.api.HMCLog;
import org.jackhuang.hmcl.util.task.TaskInfo;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DownloadFileTask extends TaskInfo {
    private String file;

    public DownloadFileTask(String file) {
        super("下载Origind文件");
        this.file = file;
    }

    @Override
    public void executeTask(boolean areDependTasksSucceeded) throws Exception {
        URL url = new URL(OrigindPlugin.getInstance().getConfig().getDownloadPrefix() + file);
        Path to = Paths.get(".minecraft", file).toAbsolutePath();
        HMCLog.log("正在下载: " + url + " 到 " + to);
        Files.copy(url.openStream(), to, StandardCopyOption.REPLACE_EXISTING);
        HMCLog.log("下载完毕: " + to);
    }
}
