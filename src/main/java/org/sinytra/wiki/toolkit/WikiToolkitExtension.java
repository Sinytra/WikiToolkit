package org.sinytra.wiki.toolkit;

import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

public abstract class WikiToolkitExtension {
    public abstract ListProperty<String> getExportedAssetNamespaces();

    public abstract RegularFileProperty getDocumentationRoot(); 

    public abstract Property<String> getWikiRepositoryUrl();

    public abstract Property<String> getWikiAccessToken();
}
