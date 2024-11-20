package org.moddedmc.wiki.toolkit.task;

import org.gradle.work.DisableCachingByDefault;

@DisableCachingByDefault
public abstract class InstallDependenciesTask extends ExecuteCommandTask {
    public InstallDependenciesTask() {
        getCommand().addAll("npm", "install");
    }
}
