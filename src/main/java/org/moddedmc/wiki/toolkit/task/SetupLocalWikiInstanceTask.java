package org.moddedmc.wiki.toolkit.task;

import org.eclipse.jgit.api.Git;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.work.DisableCachingByDefault;

import java.nio.file.Files;
import java.nio.file.Path;

@DisableCachingByDefault
public abstract class SetupLocalWikiInstanceTask extends DefaultTask {

    public SetupLocalWikiInstanceTask() {
        getOutputs().upToDateWhen(t -> false);
    }

    @Input
    public abstract Property<String> getRepositoryUrl();

    @OutputDirectory
    public abstract RegularFileProperty getWorkDir();

    @TaskAction
    public void execute() {
        Path workDir = getWorkDir().get().getAsFile().toPath();
        // Setup dir
        if (!Files.exists(workDir.resolve(".git"))) {
            try {
                setupRepository(workDir);
            } catch (Exception e) {
                getLogger().error("Error setting up local Wiki instance", e);
                throw new RuntimeException("Error setting up local Wiki instance", e);
            }
        }
        // Update
        else {
            try {
                updateRepository(workDir);
            } catch (Exception e) {
                getLogger().warn("Failed to update local Wiki instance", e);
            }
        }
    }

    private void updateRepository(Path workDir) throws Exception {
        try (Git git = Git.open(workDir.toFile())) {
            git.pull()
                .setTimeout(10)
                .call();
        }
    }

    private void setupRepository(Path workDir) throws Exception {
        getLogger().lifecycle("Setting up local Wiki instance...");

        // Auto-close git after cloning
        //noinspection EmptyTryBlock
        try (Git git = Git.cloneRepository()
            .setURI(getRepositoryUrl().get())
            .setDepth(1)
            .setDirectory(workDir.toFile())
            .call()
        ) {
            // No op
        }
    }
}
