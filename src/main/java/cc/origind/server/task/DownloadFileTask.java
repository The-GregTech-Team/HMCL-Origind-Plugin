package cc.origind.server.task;

import cc.origind.server.OrigindPlugin;
import org.jackhuang.hmcl.api.HMCLog;
import org.jackhuang.hmcl.util.task.TaskInfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

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
        Files.deleteIfExists(to);
        BufferedWriter writer = Files.newBufferedWriter(to, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        for (String s : reader.lines().collect(Collectors.toList()))
            writer.append(s);
        writer.close();
        reader.close();
        HMCLog.log("下载完毕: " + to);
    }
}
