package org.sinytra.wiki.toolkit.task;

import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.api.services.BuildService;
import org.gradle.api.services.BuildServiceParameters;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.slf4j.Slf4jOutputStream;
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ProcessExecutorService implements BuildService<BuildServiceParameters.None>, AutoCloseable {
    public static final String NAME = "wikiProcessExec";

    private final List<Process> processes = new ArrayList<>();

    public Process executeCommand(Task task, File workDir, Map<String, String> env, List<String> command, boolean silentStdOut) throws Exception {
        ProcessExecutor executor = createProcess(task, workDir, env, command, silentStdOut);
        Process process = executor.start().getProcess();
        this.processes.add(process);
        return process;
    }

    private ProcessExecutor createProcess(Task task, File workDir, Map<String, String> env, List<String> command, boolean silentStdOut) {
        Logger logger = task.getLogger();
        logger.info("Executing command {}", String.join(" ", command));
        ProcessExecutor process = new ProcessExecutor(command);
        process.environment(env);
        process.directory(workDir);
        process.redirectOutput(silentStdOut ? Slf4jStream.of(logger).asInfo() : new LifecycleOutputStream(logger));
        process.redirectError(Slf4jStream.of(logger).asError());
        return process;
    }

    @Override
    public void close() {
        for (Process process : this.processes) {
            process.descendants().forEach(ProcessHandle::destroy);
            if (process.isAlive()) {
                process.destroy();
            }
        }
    }
    
    private static class LifecycleOutputStream extends Slf4jOutputStream {
        public LifecycleOutputStream(Logger logger) {
            super(logger);
        }

        @Override
        protected void processLine(String line) {
            ((Logger) getLogger()).lifecycle(line);
        }
    }
}
