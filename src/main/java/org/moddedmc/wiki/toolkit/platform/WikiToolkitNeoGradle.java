package org.moddedmc.wiki.toolkit.platform;

import net.neoforged.gradle.dsl.common.runs.run.Run;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.NamedDomainObjectProvider;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;

import static org.moddedmc.wiki.toolkit.WikiToolkitPlugin.OUTPUT_PROPERTY;
import static org.moddedmc.wiki.toolkit.WikiToolkitPlugin.RENDER_PROPERTY;

public class WikiToolkitNeoGradle extends PlatformCommon {
    @Override
    protected void createRunModel(Project project, String name, Provider<String> namespaces, Provider<String> outputPath) {
        //noinspection unchecked
        NamedDomainObjectContainer<Run> runs = (NamedDomainObjectContainer<Run>) project.getExtensions().getByName("runs");

        NamedDomainObjectProvider<Run> model = runs.register(name, run -> {
            run.runType("client");
        });

        project.afterEvaluate(p -> {
            model.configure(run -> {
                run.systemProperty(RENDER_PROPERTY, namespaces.get());
                run.systemProperty(OUTPUT_PROPERTY, outputPath.get());
            });
        });
    }
}
