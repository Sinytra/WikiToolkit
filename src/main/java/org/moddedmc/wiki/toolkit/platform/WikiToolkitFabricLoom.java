package org.moddedmc.wiki.toolkit.platform;

import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import org.gradle.api.Project;
import org.moddedmc.wiki.toolkit.docs.DocumentationRoot;

public class WikiToolkitFabricLoom extends PlatformCommon {

    @Override
    protected void configureExporter(Project project, DocumentationRoot root) {
        LoomGradleExtensionAPI loomExtension = project.getExtensions().getByType(LoomGradleExtensionAPI.class);

        String arg = "-D%s=true".formatted(EXPORTER_ENABLED);

        loomExtension.getRuns().configureEach(r -> {
            if (r.getVmArgs().contains(arg)) {
                r.properties(getSystemProps(root));
            }
        });
    }
}
