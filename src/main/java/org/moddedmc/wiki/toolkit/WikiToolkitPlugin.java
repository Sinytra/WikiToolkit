package org.moddedmc.wiki.toolkit;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskProvider;
import org.moddedmc.wiki.toolkit.platform.PlatformCommon;
import org.moddedmc.wiki.toolkit.platform.WikiToolkitFabricLoom;
import org.moddedmc.wiki.toolkit.platform.WikiToolkitModDevGradle;
import org.moddedmc.wiki.toolkit.platform.WikiToolkitNeoGradle;
import org.moddedmc.wiki.toolkit.task.*;

import java.io.File;
import java.net.URI;

@SuppressWarnings("unused")
public abstract class WikiToolkitPlugin implements Plugin<Project> {
    public static final String RENDER_PROPERTY = "item_asset_export.render.namespaces";
    public static final String OUTPUT_PROPERTY = "item_asset_export.render.output";

    public static final String REPO_URL = "https://github.com/Sinytra/Wiki";
    public static final String DEFAULT_WIKI_URL = "https://moddedmc.wiki";

    @Override
    public void apply(Project target) {
        if (!target.getGradle().getPlugins().hasPlugin(WikiToolkitRepositoriesPlugin.class)) {
            target.getPlugins().apply(WikiToolkitRepositoriesPlugin.class);
        } else {
            target.getLogger().debug("Skipping application of Wiki Toolkit repositories plugin as it was applied at the settings level");
        }

        WikiToolkitExtension extension = target.getExtensions().create("wiki", WikiToolkitExtension.class);

        extension.origin(origin -> origin.getRepositoryUrl().convention(REPO_URL));
        target.getPluginManager().withPlugin(PlatformCommon.MOD_DEV_GRADLE_ID, p -> new WikiToolkitModDevGradle().apply(target));
        target.getPluginManager().withPlugin(PlatformCommon.NEO_GRADLE_ID, p -> new WikiToolkitNeoGradle().apply(target));
        target.getPluginManager().withPlugin(PlatformCommon.FABRIC_LOOM_GRADLE_ID, p -> new WikiToolkitFabricLoom().apply(target));
        target.getPluginManager().withPlugin(PlatformCommon.ARCH_LOOM_GRADLE_ID, p -> new WikiToolkitFabricLoom().apply(target));

        Provider<ProcessExecutorService> serviceProvider = target.getGradle().getSharedServices().registerIfAbsent(ProcessExecutorService.NAME, ProcessExecutorService.class);

        File workDir = target.getLayout().getBuildDirectory().file("previewDocs").get().getAsFile();
        target.getTasks().register("setupDocsPreview", SetupLocalWikiInstanceTask.class, t -> {
            t.setDescription("Setup local Wiki instance");
            t.getWorkDir().set(workDir);
            t.getOrigin().set(extension.getOrigin());
        });

        // TODO Check if pnpm is installed
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
            t.getDocumentationRoots().convention(extension.getDocs());

            t.dependsOn(installDepsTask);
        });

        Task revalidateDocsTask = target.getTasks().create("revalidateDocs", t -> {
            t.setGroup("publishing");
            t.setDescription("Revalidate wiki documentation for all projects to reflect new changes in source.");
            t.getOutputs().upToDateWhen(o -> false);
        });

        extension.getDocs().configureEach(root -> {
            TaskProvider<RevalidateDocsTask> task = target.getTasks().register(WikiUtils.prefixTask(root, "revalidate", "docs"), RevalidateDocsTask.class, t -> {
                t.setGroup("publishing");
                t.setDescription("Revalidate wiki documentation for '%s' to reflect new changes in source.".formatted(root.getRoot()));

                t.getTargetURI().set(URI.create(DEFAULT_WIKI_URL));
                t.getAccessToken().set(extension.getWikiAccessToken());
                t.getProjectId().set(root.getName());
            });
            revalidateDocsTask.dependsOn(task);
        });
    }
}
