package cc.origind.server;

import cc.origind.server.task.CheckVersionTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jackhuang.hmcl.api.HMCLog;
import org.jackhuang.hmcl.api.IPlugin;
import org.jackhuang.hmcl.api.auth.IAuthenticator;
import org.jackhuang.hmcl.api.func.Consumer;
import org.jackhuang.hmcl.api.ui.AddTabCallback;
import org.jackhuang.hmcl.util.task.TaskWindow;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OrigindPlugin implements IPlugin {
    private static OrigindPlugin INSTANCE;
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private OrigindConfig config;
    private CheckVersionTask checkVersionTask;

    public OrigindPlugin() {
        HMCLog.log("Loading Origind plugin...");
        loadConfig();
        checkVersionTask = new CheckVersionTask();
        INSTANCE = this;
        HMCLog.log("Loaded Origind plugin!");
    }

    private void loadConfig() {
        try {
            Path path = Paths.get("origind.json").toAbsolutePath();
            if (Files.exists(path))
                config = GSON.fromJson(Files.newBufferedReader(path, StandardCharsets.UTF_8), OrigindConfig.class);
            else {
                config = new OrigindConfig();
                Files.write(path, GSON.toJson(config, OrigindConfig.class).getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            HMCLog.err("Cannot load Origind Plugin!", e);
        }
    }

    public void saveConfig() {
        try {
            Path path = Paths.get("origind.json").toAbsolutePath();
            Files.write(path, GSON.toJson(config, OrigindConfig.class).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            HMCLog.err("Cannot save config!", e);
        }
    }

    public synchronized static OrigindPlugin getInstance() {
        return INSTANCE;
    }

    public OrigindConfig getConfig() {
        return config;
    }

    @Override
    public void onRegisterAuthenticators(Consumer<IAuthenticator> apply) {
    }

    @Override
    public void onAddTab(JFrame frame, AddTabCallback callback) {
        TaskWindow.factory().execute(checkVersionTask);
    }
}
