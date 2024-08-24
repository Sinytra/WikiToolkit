package org.sinytra.toolkit;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

@SuppressWarnings("unused")
public class WikiToolkitPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        target.getExtensions().add("wiki", WikiToolkitExtension.class);

        target.getPluginManager().withPlugin(WikiToolkitNeoForge.MOD_DEV_GRADLE_ID, p -> WikiToolkitNeoForge.apply(target));
    }
}