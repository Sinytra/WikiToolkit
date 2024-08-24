package org.sinytra.wiki.toolkit;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

@SuppressWarnings("unused")
public class WikiToolkitPlugin implements Plugin<Project> {
    public static final String RENDER_PROPERTY = "item_asset_export.render.namespaces";
    public static final String OUTPUT_PROPERTY = "item_asset_export.render.output";

    @Override
    public void apply(Project target) {
        target.getExtensions().add("wiki", WikiToolkitExtension.class);

        target.getPluginManager().withPlugin(WikiToolkitNeoForge.MOD_DEV_GRADLE_ID, p -> WikiToolkitNeoForge.apply(target));
    }
}