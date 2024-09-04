package org.sinytra.wiki.toolkit.platform;

import net.neoforged.moddevgradle.dsl.NeoForgeExtension;
import net.neoforged.moddevgradle.dsl.RunModel;
import org.gradle.api.NamedDomainObjectProvider;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;

import static org.sinytra.wiki.toolkit.WikiToolkitPlugin.OUTPUT_PROPERTY;
import static org.sinytra.wiki.toolkit.WikiToolkitPlugin.RENDER_PROPERTY;

public class WikiToolkitNeoForge extends PlatformCommon {
    @Override
    protected void createRunModel(Project project, String name, Provider<String> namespaces, Provider<String> outputPath) {
        NeoForgeExtension neoExtension = project.getExtensions().getByType(NeoForgeExtension.class);

        NamedDomainObjectProvider<RunModel> model = neoExtension.getRuns().register(name, run -> {
            run.client();
            RunModel clientRun = neoExtension.getRuns().findByName("client");
            if (clientRun != null) {
                run.getGameDirectory().set(clientRun.getGameDirectory());
            }

        });

        project.afterEvaluate(p -> model.configure(run -> {
            run.systemProperty(RENDER_PROPERTY, namespaces.get());
            run.systemProperty(OUTPUT_PROPERTY, outputPath.get());
        }));
    }
}
