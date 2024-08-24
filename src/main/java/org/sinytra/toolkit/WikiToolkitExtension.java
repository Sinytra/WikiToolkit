package org.sinytra.toolkit;

import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

public abstract class WikiToolkitExtension {
    public abstract Property<String> getItemAssetExporterVersion();

    public abstract ListProperty<String> getExportedAssetNamespaces();

    public abstract RegularFileProperty getDocumentationRoot(); 
}
