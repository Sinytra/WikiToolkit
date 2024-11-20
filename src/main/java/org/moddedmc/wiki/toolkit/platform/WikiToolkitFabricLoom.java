package org.moddedmc.wiki.toolkit.platform;

import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.configuration.ide.RunConfigSettings;
import org.gradle.api.NamedDomainObjectProvider;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;

import static org.moddedmc.wiki.toolkit.WikiToolkitPlugin.OUTPUT_PROPERTY;
import static org.moddedmc.wiki.toolkit.WikiToolkitPlugin.RENDER_PROPERTY;

public class WikiToolkitFabricLoom extends PlatformCommon {

    @Override
    protected void createRunModel(Project project, String name, Provider<String> namespaces, Provider<String> outputPath) {
        LoomGradleExtensionAPI loomExtension = project.getExtensions().getByType(LoomGradleExtensionAPI.class);

        NamedDomainObjectProvider<RunConfigSettings> model = loomExtension.getRuns().register(name, run -> {
            run.client();
            RunConfigSettings clientRun = loomExtension.getRuns().findByName("client");
            if (clientRun != null) {
                run.setRunDir(clientRun.getRunDir());
            }
        });

        project.afterEvaluate(p -> model.configure(run -> {
            run.property(RENDER_PROPERTY, namespaces.get());
            run.property(OUTPUT_PROPERTY, outputPath.get());
        }));
    }
}
