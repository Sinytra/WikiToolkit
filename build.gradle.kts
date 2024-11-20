plugins {
    java
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "1.3.0"
    id("net.neoforged.gradleutils") version "3.0.0"
}

group = "org.moddedmc.wiki"

gradleutils.version {
    branches.suffixBranch()
}
project.version = gradleutils.version
logger.lifecycle("Wiki Toolkit version ${gradleutils.version}")

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

if (System.getenv("GPP_KEY") != null) {
    project.ext {
        set("gradle.publish.key", System.getenv("GPP_KEY"))
        set("gradle.publish.secret", System.getenv("GPP_SECRET"))
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        name = "NeoForge"
        url = uri("https://maven.neoforged.net/releases")
    }
    maven {
        name = "FabricMC"
        url = uri("https://maven.fabricmc.net")
    }
}

dependencies {
    compileOnly("net.neoforged:moddev-gradle:2.0.16-beta")
    compileOnly("net.fabricmc:fabric-loom:1.7-SNAPSHOT")
    compileOnly("net.neoforged.gradle:userdev:7.0.165")

    implementation(group = "org.eclipse.jgit", name = "org.eclipse.jgit", version = "6.8.0.202311291450-r")
    implementation(group = "org.zeroturnaround", name = "zt-exec", version = "1.12")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    website.set("https://github.com/sinytra/wikitoolkit")
    vcsUrl.set("https://github.com/sinytra/wikitoolkit")
    plugins {
        create("wikiToolkitPlugin") {
            id = "org.moddedmc.wiki.toolkit"
            displayName = "ModdedMC Wiki Toolkit"
            description = "Developer toolkit for the Modded Minecraft Wiki"
            implementationClass = "org.moddedmc.wiki.toolkit.WikiToolkitPlugin"
            tags.set(listOf("minecraft", "wiki"))
        }
        create("wikiToolkitRepositoriesPlugin") {
            id = "org.moddedmc.wiki.toolkit.repositories"
            displayName = "ModdedMC Wiki Toolkit Repositories"
            description = "Configures bundled repositories for the Wiki Toolkit"
            implementationClass = "org.moddedmc.wiki.toolkit.WikiToolkitRepositoriesPlugin"
            tags.set(listOf("minecraft", "wiki"))
        }
    }
}

publishing {
    repositories {
        if (System.getenv("MAVEN_USER") != null) {
            maven {
                url = uri(System.getenv("MAVEN_URL") ?: "https://maven.sinytra.org/releases")
                credentials {
                    username = System.getenv("MAVEN_USER") ?: "not"
                    password = System.getenv("MAVEN_PASSWORD") ?: "set"
                }
            }
        }
    }
}
