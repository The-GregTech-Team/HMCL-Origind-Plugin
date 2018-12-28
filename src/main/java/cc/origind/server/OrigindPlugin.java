package cc.origind.server;

import cc.origind.server.task.CheckVersionTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OrigindPlugin extends Plugin {
    private static OrigindPlugin INSTANCE;
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private OrigindConfig config;
    private CheckVersionTask checkVersionTask;

    public OrigindPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    public void start() {
        log.info("Loading Origind plugin...");
        loadConfig();
        checkVersionTask = new CheckVersionTask();
        INSTANCE = this;
        log.info("Loaded Origind plugin!");
        checkVersionTask.start();
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
            log.error("Cannot load Origind Plugin!", e);
        }
    }

    public void saveConfig() {
        try {
            Path path = Paths.get("origind.json").toAbsolutePath();
            Files.write(path, GSON.toJson(config, OrigindConfig.class).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("Cannot save config!", e);
        }
    }

    public synchronized static OrigindPlugin getInstance() {
        return INSTANCE;
    }

    public OrigindConfig getConfig() {
        return config;
    }
}
