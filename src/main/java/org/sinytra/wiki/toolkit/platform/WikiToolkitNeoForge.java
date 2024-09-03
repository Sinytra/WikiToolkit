package org.sinytra.wiki.toolkit.platform;

import net.neoforged.moddevgradle.dsl.NeoForgeExtension;
import net.neoforged.moddevgradle.dsl.RunModel;
import org.gradle.api.NamedDomainObjectProvider;
import org.gradle.api.Project;

import static org.sinytra.wiki.toolkit.WikiToolkitPlugin.OUTPUT_PROPERTY;
import static org.sinytra.wiki.toolkit.WikiToolkitPlugin.RENDER_PROPERTY;

public class WikiToolkitNeoForge extends PlatformCommon<NamedDomainObjectProvider<RunModel>> {

    @Override
    protected void configureModel(NamedDomainObjectProvider<RunModel> model, String namespaces, String output) {
        model.configure(run -> {
            run.systemProperty(RENDER_PROPERTY, namespaces);
            run.systemProperty(OUTPUT_PROPERTY, output);
        });
    }

    @Override
    protected NamedDomainObjectProvider<RunModel> prepareRunConfig(Project project) {
        NeoForgeExtension neoExtension = project.getExtensions().getByType(NeoForgeExtension.class);

        return neoExtension.getRuns().register("assetExportClient", run -> {
            run.client();
            RunModel clientRun = neoExtension.getRuns().findByName("client");
            if (clientRun != null) {
                run.getGameDirectory().set(clientRun.getGameDirectory());
            }
        });
    }
}
