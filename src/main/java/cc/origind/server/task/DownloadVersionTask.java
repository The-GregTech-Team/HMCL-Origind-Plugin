package cc.origind.server.task;

import cc.origind.server.OrigindPlugin;
import cc.origind.server.update.Change;
import cc.origind.server.update.Update;
import cc.origind.server.update.Version;
import org.jackhuang.hmcl.api.HMCLog;
import org.jackhuang.hmcl.util.task.Task;
import org.jackhuang.hmcl.util.task.TaskInfo;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;

public class DownloadVersionTask extends TaskInfo {
    private String versionStr;
    private Version version;

    public DownloadVersionTask(Update update) {
        super("下载Origind新版本");
        this.versionStr = update.getLatestVersion();
        this.version = update.getVersionMap().get(versionStr);
    }

    @Override
    public Collection<Task> getDependTasks() {
        return version.getChanges().stream()
                .filter(change -> change.getChangeType().equals("upgrade") || change.getChangeType().equals("add"))
                .map(change -> change.getChangeType().equals("add") ? change.getFile() : change.getTo())
                .map(DownloadFileTask::new)
                .collect(Collectors.toList());
    }

    @Override
    public void executeTask(boolean areDependTasksSucceeded) throws Exception {
        for (Change change : version.getChanges()) {
            switch (change.getChangeType()) {
                case "upgrade":
                    Files.deleteIfExists(Paths.get(".minecraft", change.getFile()));
                    break;
                case "delete":
                    Files.deleteIfExists(Paths.get(".minecraft", change.getFile()));
                    break;
            }
        }
        HMCLog.log("更新完毕, 目前版本: " + versionStr);
        OrigindPlugin.getInstance().getConfig().setVersion(versionStr);
        OrigindPlugin.getInstance().saveConfig();
    }
}
