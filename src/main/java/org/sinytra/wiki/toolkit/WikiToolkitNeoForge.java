package org.sinytra.wiki.toolkit;

import net.neoforged.moddevgradle.dsl.NeoForgeExtension;
import net.neoforged.moddevgradle.dsl.RunModel;
import org.gradle.api.NamedDomainObjectProvider;
import org.gradle.api.Project;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.sinytra.wiki.toolkit.MavenUtil.ITEM_ASSET_EXPORTER_GROUP;
import static org.sinytra.wiki.toolkit.WikiToolkitPlugin.OUTPUT_PROPERTY;
import static org.sinytra.wiki.toolkit.WikiToolkitPlugin.RENDER_PROPERTY;

public class WikiToolkitNeoForge {
    public static final String MOD_DEV_GRADLE_ID = "net.neoforged.moddev";
    public static final String ITEM_ASSET_EXPORTER_NAME = "item-asset-export-neoforge";

    public static void apply(Project project) {
        NeoForgeExtension neoExtension = project.getExtensions().getByType(NeoForgeExtension.class);
        WikiToolkitExtension extension = project.getExtensions().getByType(WikiToolkitExtension.class);

        if (extension.getExportedAssetNamespaces().isPresent()) {
            NamedDomainObjectProvider<RunModel> model = neoExtension.getRuns().register("assetExportClient", run -> {
                run.client();
                RunModel clientRun = neoExtension.getRuns().findByName("client");
                if (clientRun != null) {
                    run.getGameDirectory().set(clientRun.getGameDirectory());
                }
            });

            project.afterEvaluate(p -> {
                String exporterVersion = getItemAssetExporterVersion(p);
                project.getLogger().info("Found asset exporter version {}", exporterVersion);
                if (exporterVersion != null) {
                    Path path = extension.getDocumentationRoot().get().getAsFile().toPath().resolve(".assets/item");
                    try {
                        Files.createDirectories(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    project.getDependencies().add("runtimeOnly", project.getDependencyFactory().create(ITEM_ASSET_EXPORTER_GROUP, ITEM_ASSET_EXPORTER_NAME, exporterVersion));
                    model.configure(run -> {
                        run.systemProperty(RENDER_PROPERTY, String.join(",", extension.getExportedAssetNamespaces().get()));
                        run.systemProperty(OUTPUT_PROPERTY, path.toAbsolutePath().toString());
                    });
                }
            });
        }
    }

    private static String getItemAssetExporterVersion(Project project) {
        WikiToolkitExtension extension = project.getExtensions().getByType(WikiToolkitExtension.class);
        if (extension.getItemAssetExporterVersion().isPresent()) {
            return extension.getItemAssetExporterVersion().get();
        }

        try {
            String mcVersion = getMinecraftVersion(project);
            List<String> versions = MavenUtil.getAvailableModVersions(ITEM_ASSET_EXPORTER_NAME);
            List<String> availableVersions = versions.stream()
                .filter(s -> s.split("\\+")[1].equals(mcVersion))
                .sorted()
                .toList();
            if (!availableVersions.isEmpty()) {
                return availableVersions.getLast();
            }
        } catch (Throwable e) {
            project.getLogger().error("Error fetching available versions", e);
        }
        return null;
    }

    @Nullable
    private static String getMinecraftVersion(Project project) {
        NeoForgeExtension neoExtension = project.getExtensions().getByType(NeoForgeExtension.class);
        if (!neoExtension.getVersion().isPresent()) {
            project.getLogger().error("Missing NeoForge version, skipping toolkit setup");
            return null;
        }
        // Determine mc version
        String neoVersion = neoExtension.getVersion().get();
        String[] parts = neoVersion.split("\\.");
        String mcVersion = "1." + parts[0];
        if (!parts[1].equals("0")) {
            mcVersion += "." + parts[1];
        }
        return mcVersion;
    }
}
