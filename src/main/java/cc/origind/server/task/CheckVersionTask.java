package cc.origind.server.task;

import cc.origind.server.OrigindConfig;
import cc.origind.server.OrigindPlugin;
import cc.origind.server.update.Update;
import com.google.gson.Gson;
import org.jackhuang.hmcl.api.HMCLog;
import org.jackhuang.hmcl.util.task.Task;
import org.jackhuang.hmcl.util.task.TaskInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

public class CheckVersionTask extends TaskInfo {
    private Update update;

    public CheckVersionTask() {
        super("检测Origind服务器更新");
    }

    @Override
    public void executeTask(boolean areDependTasksSucceeded) throws Exception {
        OrigindConfig config = OrigindPlugin.getInstance().getConfig();
        Gson gson = new Gson();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(config.getUpdateJsonURL()).openStream(), StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        reader.lines().forEach(stringBuilder::append);
        reader.close();
        Update update = gson.fromJson(stringBuilder.toString(), Update.class);
        if (!config.getVersion().equals(update.getLatestVersion())) this.update = update;
        HMCLog.log("最新版本: " + update.getLatestVersion());
    }

    @Override
    public Collection<Task> getAfterTasks() {
        return update == null ? null : Collections.singletonList(new DownloadVersionTask(update));
    }
}
