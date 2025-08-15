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

Running a local preview requires [NodeJS](https://nodejs.org/en/download/) and [pnpm](https://pnpm.io/installation).
Please make sure you have NodeJS **v20 or above** installed on your computer.

**To start the local preview, run the `previewDocs` task.** This will set up and launch the local wiki environment.

Once ready, it will be available at `http://localhost:3000`.

### Known issues

Using `org.gradle.daemon=false` will prevent the npm task from existing when gradle is stopped, keeping it running
in the background. Please check your `gradle.properties` file and make sure this property is either set to `true` or
removed entirely.

## Exporting assets and game data

> [!NOTE]  
> Exporting assets is only supported when using [ModDevGradle](https://github.com/neoforged/ModDevGradle) (NeoForge) or
> [Fabric Loom](https://github.com/FabricMC/fabric-loom) (Fabric)

Our data export mod automatically renders and exports images of in-game items for you the way they are displayed
in one's inventory.

When combined with this plugin, resulting files are placed in the appropriate subfolder inside your documentation root,
**ready to be referenced** in docs with no additional configuration necessary.

### Usage

Install and configure the exporter as described in its [repository](https://github.com/Sinytra/WikiDataExporter).

You don't need to set the output path - we'll do that for you automatically.

To run the export, launch the appropriate game run(s) where the exporter is enabled.

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
