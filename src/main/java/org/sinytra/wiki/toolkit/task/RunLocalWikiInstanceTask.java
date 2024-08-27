package org.sinytra.wiki.toolkit.task;

import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.work.DisableCachingByDefault;

@DisableCachingByDefault
public abstract class RunLocalWikiInstanceTask extends ExecuteCommandTask {
    public static final String LOCAL_PREVIEW_ENV = "ENABLE_LOCAL_PREVIEW";
    public static final String LOCAL_DOCS_ROOTS = "LOCAL_DOCS_ROOTS";

    @InputDirectory
    public abstract RegularFileProperty getLocalDocsDir();

    public RunLocalWikiInstanceTask() {
        getCommand().addAll("npm", "run", "dev");
        getEnvironment().put(LOCAL_PREVIEW_ENV, "true");
        getEnvironment().put(LOCAL_DOCS_ROOTS, getLocalDocsDir().map(r -> r.getAsFile().getAbsolutePath()));
    }
}
