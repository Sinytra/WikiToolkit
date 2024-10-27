package org.sinytra.wiki.toolkit;

import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle;

// Credit https://github.com/lukebemishprojects/crochet/blob/e20ed354e9f1e078ab0f1fd82e58a86b2fe3c33b/src/main/java/dev/lukebemish/crochet/internal/CrochetRepositoriesPlugin.java
@SuppressWarnings("unused")
public class WikiToolkitRepositoriesPlugin implements Plugin<Object> {
    public static final String MAVEN_URL = "https://maven.sinytra.org/";

    @Override
    public void apply(Object target) {
        if (target instanceof Project project) {
            repositories(project.getRepositories());
        } else if (target instanceof Settings settings) {
            repositories(settings.getDependencyResolutionManagement().getRepositories());
            settings.getGradle().getPlugins().apply(WikiToolkitRepositoriesPlugin.class);
        } else if (!(target instanceof Gradle)) {
            throw new GradleException("This plugin does not support being applied to " + target);
        }
    }

    private static void repositories(RepositoryHandler repositoryHandler) {
        repositoryHandler.maven(repo -> {
            repo.setName("Sinytra");
            repo.setUrl(MAVEN_URL);
            repo.content(r -> {
                r.includeGroup("org.sinytra");
                r.includeGroup("org.sinytra.wiki");
            });
        });
    }
}
