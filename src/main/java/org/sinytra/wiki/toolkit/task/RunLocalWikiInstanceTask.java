package org.sinytra.wiki.toolkit.task;

import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.work.DisableCachingByDefault;
import org.sinytra.wiki.toolkit.docs.DocumentationRoot;

import java.util.stream.Collectors;

@DisableCachingByDefault
public abstract class RunLocalWikiInstanceTask extends ExecuteCommandTask {
    public static final String LOCAL_PREVIEW_ENV = "ENABLE_LOCAL_PREVIEW";
    public static final String LOCAL_DOCS_ROOTS = "LOCAL_DOCS_ROOTS";

    @InputDirectory
    public abstract SetProperty<DocumentationRoot> getDocumentationRoots();

    public RunLocalWikiInstanceTask() {
        getCommand().addAll("npm", "run", "dev");
        getEnvironment().put(LOCAL_PREVIEW_ENV, "true");
        getEnvironment().put(LOCAL_DOCS_ROOTS, getDocumentationRoots().get().stream().map(r -> r.getRoot().get().getAsFile().getAbsolutePath()).collect(Collectors.joining(";")));
    }
}
