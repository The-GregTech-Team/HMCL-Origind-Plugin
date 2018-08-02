package cc.origind.server.task;

import cc.origind.server.OrigindPlugin;
import cc.origind.server.update.Change;
import cc.origind.server.update.Update;
import cc.origind.server.update.Version;
import cc.origind.server.util.ChangeUtil;
import org.jackhuang.hmcl.api.HMCLog;
import org.jackhuang.hmcl.util.task.Task;
import org.jackhuang.hmcl.util.task.TaskInfo;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DownloadVersionTask extends TaskInfo {
    private String versionStr;
    private Version version;
    private List<Version> oldVersions;

    public DownloadVersionTask(Update update) {
        super("下载Origind新版本");
        this.versionStr = update.getLatestVersion();
        this.version = update.getVersions()
                .stream()
                .filter(ver -> ver.getId().equals(versionStr))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Cannot find specific version: " + versionStr));
        Version oldVersion = update.getVersions()
                .stream()
                .filter(ver -> ver.getId().equals(OrigindPlugin.getInstance().getConfig().getVersion()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Cannot find old version: " + OrigindPlugin.getInstance().getConfig().getVersion()));
        for (int i = update.getVersions().indexOf(oldVersion) + 1; i >= update.getVersions().indexOf(version); i--) {
            oldVersions.add(update.getVersions().get(i));
        }
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
        List<Change> changes = new ArrayList<>();
        oldVersions.stream().map(Version::getChanges).forEach(changes::addAll);

        for (Change change : ChangeUtil.merge(changes)) {
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
