package org.moddedmc.wiki.toolkit.docs;

import org.gradle.api.Named;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.ListProperty;

public abstract class DocumentationRoot implements Named {
    @Override
    public abstract String getName();

    public abstract RegularFileProperty getRoot();

    public abstract ListProperty<String> getExportedAssetNamespaces();
}
