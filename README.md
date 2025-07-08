# Sinytra Wiki Dev Toolkit

Utility gradle plugin to aid in authoring documentation on the Sinytra Modded MC Wiki.

Highlighted features:

- Local real-time, accurate preview of your documentation in the exact same look as it will have once uploaded to the wiki
- Automatically generating asset renders of inventory models for all mod items
- Revalidating documentation after publishing

## Installation

The wiki toolkit plugin is available on the [Gradle Plugin Portal](https://plugins.gradle.org/plugin/org.moddedmc.wiki.toolkit).

You can install it in your project by adding the following code to your `build.gradle` or `build.gradle.kts`.

`build.gradle`
```groovy
plugins {
    id 'org.moddedmc.wiki.toolkit' version '<version>'
}
```

`build.gradle.kts`
```kts
plugins {
    id("org.moddedmc.wiki.toolkit") version "<version>"
}
```


## Setup

The initial step for using this plugin is registering documentation roots, which is done via a Gradle DSL.

A minimal setup may look like this:

`build.gradle`
```groovy
wiki {
    docs {
        // The name of the object (examplemod) should match the registered wiki project ID (if it exists).
        examplemod {
            // The path to the folder containing the documentation metadata file (sinytra-wiki.json)
            root = file('docs/examplemod')
        }
    }
}
```

`build.gradle.kts`
```kts
wiki {
    // The name of the object (examplemod) should match the registered wiki project ID (if it exists).
    docs.create("examplemod") {
        // The path to the folder containing the documentation metadata file (sinytra-wiki.json)
        root = file("docs/examplemod")
    }
}
```

## Real-time documentation preview

The plugin allows you to preview your mod documentation in real-time on your local machine. Behind the scenes,
this is done by cloning the Wiki's GitHub repository and enabling local preview mode.

Running a local preview requires [NodeJS](https://nodejs.org/en/download/). Please make sure you have NodeJS
**v20 or above** installed on your computer.

**To start the local preview, run the `previewDocs` task.** This will set up and launch the local wiki environment.

Once ready, it will be available at `http://localhost:3000`.

### Known issues

Using `org.gradle.daemon=false` will prevent the npm task from existing when gradle is stopped, keeping it running
in the background. Please check your `gradle.properties` file and make sure this property is either set to `true` or
removed entirely.

## Exporting assets

> [!NOTE]  
> Exporting assets is only supported when using [ModDevGradle](https://github.com/neoforged/ModDevGradle) (NeoForge) or [Fabric Loom](https://github.com/FabricMC/fabric-loom) (Fabric)

This feature lets you automatically render and export images of in-game items the way they are displayed
in one's inventory, saving you time preparing documentation assets.

The resulting files are placed in the appropriate subfolder inside your documentation root, **ready to be referenced**
in docs with no additional configuration necessary.

### Prerequisites

Before running an export, make sure you have configured the `exportedAssetNamespaces` property of your
documentation root(s).

```groovy
wiki {
    docs {
        examplemod {
            // Namespaces of registered in-game item IDs that you wish to include in the asset export
            // for this documentation root.
            exportedAssetNamespaces = ['examplemod']
        }
    }
}
```

Next, install our [Item Asset Exporter](https://github.com/Sinytra/ItemAssetExporterMod) mod.
There's no need to worry about it being present in all game runs, as it does not do anything by default
nor does it affect the game. The exporter code only activates on designated run configurations.

```groovy
// Adding a repository is not necessary; it is automatically installed by the plugin.
// However, if you are using a multiloader setup like Architectury Loom, you will need to install the plugin on your subprojects as well.
dependencies {
    // ModDevGradle
    // Version list: https://maven.sinytra.org/org/sinytra/wiki-exporter-neoforge
    runtimeOnly "org.sinytra:wiki-exporter-neoforge:<version>"

    // Fabric Loom
    // Version list: https://maven.sinytra.org/org/sinytra/wiki-exporter-fabric
    modRuntimeOnly "org.sinytra:wiki-exporter-fabric:<version>"
}
```

<details>
<summary>See instructions for 1.21.4 and below</summary>

```groovy
// Adding a repository is not necessary; it is automatically installed by the plugin.
// However, if you are using a multiloader setup like Architectury Loom, you will need to install the plugin on your subprojects as well.
dependencies {
    // ModDevGradle
    // Version list: https://maven.sinytra.org/org/sinytra/item-asset-export-neoforge
    runtimeOnly "org.sinytra:item-asset-export-neoforge:<version>"

    // Fabric Loom
    // Version list: https://maven.sinytra.org/org/sinytra/item-asset-export-fabric
    modRuntimeOnly "org.sinytra:item-asset-export-fabric:<version>"
}
```

</details>

### Running the export

Export tasks are created for each documentation root as automated client game runs. They will launch a game client that
once initialized, exports the renders of all matching items and exists the game. **Please do not interact with the client
during this run.** If the game reaches the main menu, please open a bug report on this repository.

To run an export for a documentation root, use the `runExport<name>Assets`, where `<name>` is the
root's capitalized name, e.g. `runExportExamplemodAssets`.

## Publishing documentation

As the wiki caches all pages once they've been visited, updates to the documentation source will not be
reflected on the website until the cache has been periodically revalidated, which can take a long time.

To force all of your documentation pages to re-render the next time they're visited
(effectively "publishing" your documentation), the plugin provides a straightforward way to submit a request to our API.

### Generating a token

Before you run the task, generate a GitHub [Personal access token (classic)](https://github.com/settings/tokens/new)
with no permissions. Copy the generated token and add it to your gradle configuration under the `wiki` block.

```groovy
wiki {
    wikiAccessToken = "MY_ACCESS_TOKEN" // Starts with ghp_
}
```

### Running the task

To revalidate a single documentation root, run `revalidate<name>Docs` where `<name` is the
root's capitalized name, e.g. `revalidateExamplemodDocs`.

To revalidate **all roots**, run the `revalidateDocs` task.
