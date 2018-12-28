package cc.origind.server.task;

import cc.origind.server.OrigindConfig;
import cc.origind.server.OrigindPlugin;
import cc.origind.server.update.Update;
import com.google.gson.Gson;
import org.jackhuang.hmcl.task.Task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

import static org.jackhuang.hmcl.util.Logging.LOG;

public class CheckVersionTask extends Task {
    private Update update;

    public CheckVersionTask() {
        this.setName("检测Origind服务器更新");
    }

    @Override
    public void execute() throws Exception {
        OrigindConfig config = OrigindPlugin.getInstance().getConfig();
        Gson gson = new Gson();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(config.getUpdateJsonURL()).openStream(), StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        reader.lines().forEach(stringBuilder::append);
        reader.close();
        LOG.info(stringBuilder.toString());
        Update update = gson.fromJson(stringBuilder.toString(), Update.class);
        if (!config.getVersion().equals(update.getLatestVersion())) this.update = update;
        LOG.info("最新版本: " + update.getLatestVersion());
        updateMessage("最新版本: " + update.getLatestVersion());
    }

    @Override
    public Collection<? extends Task> getDependencies() {
        return Collections.singleton(new DownloadVersionTask(update));
    }
}
