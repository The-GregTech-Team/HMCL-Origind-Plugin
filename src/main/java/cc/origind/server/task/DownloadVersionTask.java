package cc.origind.server.task;

import cc.origind.server.OrigindPlugin;
import cc.origind.server.update.Change;
import cc.origind.server.update.Update;
import cc.origind.server.update.Version;
import cc.origind.server.util.ChangeUtil;
import org.jackhuang.hmcl.Metadata;
import org.jackhuang.hmcl.task.FileDownloadTask;
import org.jackhuang.hmcl.task.Task;
import org.jackhuang.hmcl.util.Logging;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class DownloadVersionTask extends Task {
    private String versionStr;
    private Version version;
    private Set<Version> oldVersions = new LinkedHashSet<>();

    public DownloadVersionTask(Update update) {
        setName("下载Origind新版本");
        this.versionStr = update.getLatestVersion();
        this.version = update.getVersions()
                .stream()
                .filter(ver -> ver.getId().equals(versionStr))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot find specific version: " + versionStr));
        Version oldVersion = update.getVersions()
                .stream()
                .filter(ver -> ver.getId().equals(OrigindPlugin.getInstance().getConfig().getVersion()))
                .findFirst()
                .orElse(null);
        oldVersions.add(version);
        for (int i = update.getVersions().indexOf(oldVersion) + 1; i >= update.getVersions().indexOf(version) && i < update.getVersions().size(); i--) {
            oldVersions.add(update.getVersions().get(i));
        }
    }

    @Override
    public void execute() throws Exception {
        Logging.LOG.info(getChanges().toString());

        for (Change change : getChanges()) {
            switch (change.getChangeType()) {
                case "upgrade":
                case "delete":
                    Path file = Metadata.MINECRAFT_DIRECTORY.resolve(change.getFile());
                    if (Files.deleteIfExists(file))
                        Logging.LOG.info("Removed " + file);
                    else
                        Logging.LOG.info("Not removing non-exist file " + file);
                    break;
            }
        }
        Logging.LOG.info("更新完毕, 目前版本: " + versionStr);
        OrigindPlugin.getInstance().getConfig().setVersion(versionStr);
        OrigindPlugin.getInstance().saveConfig();
    }

    public Collection<Change> getChanges() {
        Collection<Change> changes = new ArrayList<>();
        oldVersions.stream().map(Version::getChanges).forEach(changes::addAll);
        return oldVersions.size() <= 1 ? changes : ChangeUtil.merge(changes);
    }

    @Override
    public Collection<? extends Task> getDependents() {
        return getChanges().stream()
                .filter(change -> change.getChangeType().equals("upgrade") || change.getChangeType().equals("add"))
                .map(change -> change.getChangeType().equals("add") ? change.getFile() : change.getTo())
                .map(s -> {
                    try {
                        return new FileDownloadTask(new URL(OrigindPlugin.getInstance().getConfig().getDownloadPrefix() + s), Metadata.MINECRAFT_DIRECTORY.resolve(s).toFile());
                    } catch (MalformedURLException e) {
                        Logging.LOG.log(Level.WARNING, "Cannot download file " + s, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
