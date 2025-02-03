package org.moddedmc.wiki.toolkit.task;

import org.gradle.internal.os.OperatingSystem;
import org.gradle.work.DisableCachingByDefault;

@DisableCachingByDefault
public abstract class InstallDependenciesTask extends ExecuteCommandTask {
    public InstallDependenciesTask() {
        if (OperatingSystem.current().isLinux()) {
            getCommand().addAll("bash", "-ci", "npm install");
        } else if (OperatingSystem.current().isMacOsX()) {
            getCommand().addAll("bash", "-c", "npm install");
        } else {
            getCommand().addAll("npm", "install");
        }
    }
}
