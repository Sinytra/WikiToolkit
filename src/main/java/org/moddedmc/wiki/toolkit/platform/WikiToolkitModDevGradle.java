package org.moddedmc.wiki.toolkit.platform;

import net.neoforged.moddevgradle.dsl.NeoForgeExtension;
import org.gradle.api.Project;
import org.moddedmc.wiki.toolkit.docs.DocumentationRoot;

public class WikiToolkitModDevGradle extends PlatformCommon {
    @Override
    protected void configureExporter(Project project, DocumentationRoot root) {
        NeoForgeExtension neoExtension = project.getExtensions().getByType(NeoForgeExtension.class);

        neoExtension.getRuns().configureEach(r -> {
            if ("true".equals(r.getSystemProperties().get().get(EXPORTER_ENABLED))) {
                r.getSystemProperties().putAll(getSystemProps(root));
            }
        });
    }
}
