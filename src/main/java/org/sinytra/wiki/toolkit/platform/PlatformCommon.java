package org.sinytra.wiki.toolkit.platform;

import org.gradle.api.Project;
import org.gradle.api.provider.Provider;
import org.sinytra.wiki.toolkit.WikiToolkitExtension;
import org.sinytra.wiki.toolkit.WikiUtils;

public abstract class PlatformCommon {
    public static final String MOD_DEV_GRADLE_ID = "net.neoforged.moddev";
    public static final String FABRIC_LOOM_GRADLE_ID = "fabric-loom";
    public static final String MAVEN_URL = "https://maven.su5ed.dev/releases/";

    protected abstract void createRunModel(Project project, String name, Provider<String> namespaces, Provider<String> outputPath);

    public void apply(Project project) {
        WikiToolkitExtension extension = project.getExtensions().getByType(WikiToolkitExtension.class);

        PlatformCommon.setupMavenRepository(project);

        extension.getDocs().all(root -> {
            if (root.getExportedAssetNamespaces().isPresent()) {
                Provider<String> namespaces = root.getExportedAssetNamespaces().map(s -> String.join(",", s));
                Provider<String> path = root.getRoot().map(f -> f.getAsFile().toPath().resolve(".assets/item").toAbsolutePath().toString());

                String name = WikiUtils.prefixTask(root, "export", "assets");
                createRunModel(project, name, namespaces, path);
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
