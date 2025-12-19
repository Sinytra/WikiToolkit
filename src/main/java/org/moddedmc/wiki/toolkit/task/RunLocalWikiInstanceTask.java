package org.moddedmc.wiki.toolkit.task;

import org.gradle.internal.os.OperatingSystem;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.Input;
import org.gradle.work.DisableCachingByDefault;
import org.moddedmc.wiki.toolkit.docs.DocumentationRoot;

import java.util.Map;
import java.util.stream.Collectors;

@DisableCachingByDefault
public abstract class RunLocalWikiInstanceTask extends ExecuteCommandTask {
    public static final String LOCAL_DOCS_ROOTS = "LOCAL_DOCS_ROOTS";
    public static final String PKG_NAME = "@sinytra/wiki-previewer@latest";

    @Input
    public abstract SetProperty<DocumentationRoot> getDocumentationRoots();

    public RunLocalWikiInstanceTask() {
        if (OperatingSystem.current().isLinux()) {
            getCommand().addAll("bash", "-ci", "npx -y " + PKG_NAME);
        } else if (OperatingSystem.current().isMacOsX()) {
            getCommand().addAll("bash", "-c", "npx -y " + PKG_NAME);
        } else {
            getCommand().addAll("npx", "-y", PKG_NAME);
        }
    }

    @Override
    protected void modifyEnvironment(Map<String, String> env) {
        super.modifyEnvironment(env);

        env.put(LOCAL_DOCS_ROOTS, getDocumentationRoots().get().stream().map(r -> r.getRoot().get().getAsFile().getAbsolutePath()).collect(Collectors.joining(";")));
    }
}
