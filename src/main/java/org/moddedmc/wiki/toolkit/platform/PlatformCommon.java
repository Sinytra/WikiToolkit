package org.moddedmc.wiki.toolkit.platform;

import org.gradle.api.Project;
import org.moddedmc.wiki.toolkit.WikiToolkitExtension;
import org.moddedmc.wiki.toolkit.docs.DocumentationRoot;

import java.nio.file.Path;
import java.util.Map;

public abstract class PlatformCommon {
    public static final String MOD_DEV_GRADLE_ID = "net.neoforged.moddev";
    public static final String NEO_GRADLE_ID = "net.neoforged.gradle.userdev";
    public static final String FABRIC_LOOM_GRADLE_ID = "fabric-loom";
    public static final String ARCH_LOOM_GRADLE_ID = "dev.architectury.loom";

    public static final String EXPORTER_ENABLED = "wiki_exporter.enabled";

    protected abstract void configureExporter(Project project, DocumentationRoot root);

    public void apply(Project project) {
        WikiToolkitExtension extension = project.getExtensions().getByType(WikiToolkitExtension.class);

        project.afterEvaluate(p -> {
            if (extension.getDocs().size() == 1) {
                extension.getDocs().all(root -> {
                    configureExporter(p, root);
                });
            }
        });
    }

    protected Map<String, String> getSystemProps(DocumentationRoot root) {
        Path docsRoot = root.getRoot().getAsFile().get().toPath();
        Path assetsRoot = docsRoot.resolve(".assets");
        Path dataRoot = docsRoot.resolve(".data");

        return Map.of(
            "wiki_exporter.module.render.output.path", assetsRoot.toAbsolutePath().toString(),
            "wiki_exporter.module.metadata.output.path", dataRoot.toAbsolutePath().toString()
        );
    }
}
