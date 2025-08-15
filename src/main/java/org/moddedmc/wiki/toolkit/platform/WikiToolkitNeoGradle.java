package org.moddedmc.wiki.toolkit.platform;

import net.neoforged.gradle.dsl.common.runs.run.Run;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.moddedmc.wiki.toolkit.docs.DocumentationRoot;

public class WikiToolkitNeoGradle extends PlatformCommon {
    @Override
    protected void configureExporter(Project project, DocumentationRoot root) {
        //noinspection unchecked
        NamedDomainObjectContainer<Run> runs = (NamedDomainObjectContainer<Run>) project.getExtensions().getByName("runs");

        runs.configureEach(r -> {
            if ("true".equals(r.getSystemProperties().get().get(EXPORTER_ENABLED))) {
                r.getSystemProperties().putAll(getSystemProps(root));
            }
        });
    }
}
