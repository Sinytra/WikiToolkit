package org.sinytra.wiki.toolkit.platform;

import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.configuration.ide.RunConfigSettings;
import org.gradle.api.NamedDomainObjectProvider;
import org.gradle.api.Project;

import static org.sinytra.wiki.toolkit.WikiToolkitPlugin.OUTPUT_PROPERTY;
import static org.sinytra.wiki.toolkit.WikiToolkitPlugin.RENDER_PROPERTY;

public class WikiToolkitFabricLoom extends PlatformCommon<NamedDomainObjectProvider<RunConfigSettings>> {

    @Override
    protected NamedDomainObjectProvider<RunConfigSettings> prepareRunConfig(Project project) {
        LoomGradleExtensionAPI loomExtension = project.getExtensions().getByType(LoomGradleExtensionAPI.class);

        return loomExtension.getRuns().register("assetExportClient", run -> {
            run.client();
            RunConfigSettings clientRun = loomExtension.getRuns().findByName("client");
            if (clientRun != null) {
                run.setRunDir(clientRun.getRunDir());
            }
        });
    }

    @Override
    protected void configureModel(NamedDomainObjectProvider<RunConfigSettings> model, String namespaces, String output) {
        model.configure(run -> {
            run.property(RENDER_PROPERTY, namespaces);
            run.property(OUTPUT_PROPERTY, output);
        });
    }
}
