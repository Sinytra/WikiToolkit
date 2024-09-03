package org.sinytra.wiki.toolkit;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskProvider;
import org.sinytra.wiki.toolkit.platform.PlatformCommon;
import org.sinytra.wiki.toolkit.platform.WikiToolkitFabricLoom;
import org.sinytra.wiki.toolkit.platform.WikiToolkitNeoForge;
import org.sinytra.wiki.toolkit.task.InstallDependenciesTask;
import org.sinytra.wiki.toolkit.task.ProcessExecutorService;
import org.sinytra.wiki.toolkit.task.RunLocalWikiInstanceTask;
import org.sinytra.wiki.toolkit.task.SetupLocalWikiInstanceTask;

import java.io.File;

@SuppressWarnings("unused")
public abstract class WikiToolkitPlugin implements Plugin<Project> {
    public static final String RENDER_PROPERTY = "item_asset_export.render.namespaces";
    public static final String OUTPUT_PROPERTY = "item_asset_export.render.output";

    public static final String REPO_URL = "https://github.com/Sinytra/Wiki";

    @Override
    public void apply(Project target) {
        WikiToolkitExtension extension = target.getExtensions().create("wiki", WikiToolkitExtension.class);

        extension.getWikiRepositoryUrl().convention(REPO_URL);
        target.getPluginManager().withPlugin(PlatformCommon.MOD_DEV_GRADLE_ID, p -> new WikiToolkitNeoForge().apply(target));
        target.getPluginManager().withPlugin(PlatformCommon.FABRIC_LOOM_GRADLE_ID, p -> new WikiToolkitFabricLoom().apply(target));

        Provider<ProcessExecutorService> serviceProvider = target.getGradle().getSharedServices().registerIfAbsent(ProcessExecutorService.NAME, ProcessExecutorService.class);

        File workDir = target.getLayout().getBuildDirectory().file("previewDocs").get().getAsFile();
        target.getTasks().register("setupDocsPreview", SetupLocalWikiInstanceTask.class, t -> {
            t.setDescription("Setup local Wiki instance");
            t.getWorkDir().set(workDir);
            t.getRepositoryUrl().set(extension.getWikiRepositoryUrl());
        });

        TaskProvider<InstallDependenciesTask> installDepsTask = target.getTasks().register("installDocsDependencies", InstallDependenciesTask.class, t -> {
            t.usesService(serviceProvider);
            t.getExecService().convention(serviceProvider);

            t.doNotTrackState("NPM tracks the state");
            t.getSilentStdOut().set(true);
            t.getWorkingDir().set(workDir);

            t.dependsOn("setupDocsPreview");
        });

        // TODO File watcher
        target.getTasks().register("previewDocs", RunLocalWikiInstanceTask.class, t -> {
            t.setGroup("documentation");
            t.setDescription("Runs a local Wiki instance");

            t.usesService(serviceProvider);
            t.getExecService().convention(serviceProvider);

            t.getWorkingDir().set(workDir);
            t.getLocalDocsDir().set(extension.getDocumentationRoot());

            t.dependsOn(installDepsTask);
        });
    }
}