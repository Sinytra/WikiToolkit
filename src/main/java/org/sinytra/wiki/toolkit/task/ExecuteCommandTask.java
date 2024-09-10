package org.sinytra.wiki.toolkit.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;
import org.gradle.internal.os.OperatingSystem;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ExecuteCommandTask extends DefaultTask {

    public ExecuteCommandTask() {
        getSilentStdOut().convention(false);
    }

    @Internal
    public abstract Property<ProcessExecutorService> getExecService();

    @InputDirectory
    public abstract RegularFileProperty getWorkingDir();

    @Input
    public abstract ListProperty<String> getCommand();

    @Input
    public abstract MapProperty<String, String> getEnvironment();

    @Input
    public abstract Property<Boolean> getSilentStdOut();

    @TaskAction
    public void execute() throws Exception {
        Logger logger = getLogger();
        File workDir = getWorkingDir().get().getAsFile();

        boolean isWindows = OperatingSystem.current().isWindows();
        List<String> cmd = new ArrayList<>();
        if (isWindows) {
            cmd.addAll(List.of("cmd.exe", "/c"));
        }
        cmd.addAll(getCommand().get());

        ProcessExecutorService execService = getExecService().get();
        Map<String, String> env = new HashMap<>(getEnvironment().get());
        modifyEnvironment(env);
        Process process = execService.executeCommand(this, workDir, env, cmd, getSilentStdOut().get());
        process.waitFor();

        if (process.exitValue() != 0) {
            logger.warn("Command '{}' failed with exit code {}", cmd, process.exitValue());
        } else {
            logger.info("Command '{}' completed successfully", cmd);
        }
    }

    protected void modifyEnvironment(Map<String, String> env) {}
}
