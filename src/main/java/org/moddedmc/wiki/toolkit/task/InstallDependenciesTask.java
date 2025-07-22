package org.moddedmc.wiki.toolkit.task;

import org.gradle.internal.os.OperatingSystem;
import org.gradle.work.DisableCachingByDefault;

@DisableCachingByDefault
public abstract class InstallDependenciesTask extends ExecuteCommandTask {
    public InstallDependenciesTask() {
        if (OperatingSystem.current().isLinux()) {
            getCommand().addAll("bash", "-ci", "pnpm install");
        } else if (OperatingSystem.current().isMacOsX()) {
            getCommand().addAll("bash", "-c", "pnpm install");
        } else {
            getCommand().addAll("pnpm", "install");
        }
    }
}
