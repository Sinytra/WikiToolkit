package org.sinytra.wiki.toolkit.platform;

import org.gradle.api.Project;
import org.sinytra.wiki.toolkit.WikiToolkitExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class PlatformCommon<R> {
    public static final String MOD_DEV_GRADLE_ID = "net.neoforged.moddev";
    public static final String FABRIC_LOOM_GRADLE_ID = "fabric-loom";
    public static final String MAVEN_URL = "https://maven.su5ed.dev/releases/";

    protected abstract R prepareRunConfig(Project project);

    protected abstract void configureModel(R model, String namespaces, String output);

    public void apply(Project project) {
        WikiToolkitExtension extension = project.getExtensions().getByType(WikiToolkitExtension.class);

        R model = prepareRunConfig(project);

        PlatformCommon.setupMavenRepository(project);

        project.afterEvaluate(p -> {
            if (extension.getDocumentationRoot().isPresent() && extension.getExportedAssetNamespaces().isPresent()) {
                Path path = extension.getDocumentationRoot().get().getAsFile().toPath().resolve(".assets/item");
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                configureModel(model, String.join(",", extension.getExportedAssetNamespaces().get()), path.toAbsolutePath().toString());
            }
        });
    }

    public static void setupMavenRepository(Project p) {
        p.getRepositories().maven(repo -> {
            repo.setName("Sinytra");
            repo.setUrl(MAVEN_URL);
            repo.content(r -> {
                r.includeGroup("org.sinytra");
                r.includeGroup("org.sinytra.wiki");
            });
        });
    }
}
