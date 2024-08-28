plugins {
    java
    `java-gradle-plugin`
    `maven-publish`
    id("net.neoforged.gradleutils") version "3.0.0"
}

group = "org.sinytra.wiki"

gradleutils.version {
    branches.suffixBranch()
}
project.version = gradleutils.version
logger.lifecycle("Wiki Toolkit version ${gradleutils.version}")

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        name = "NeoForge"
        url = uri("https://maven.neoforged.net/releases")
    }
}

dependencies {
    implementation("net.neoforged:moddev-gradle:2.0.16-beta")

    implementation(group = "org.eclipse.jgit", name = "org.eclipse.jgit", version = "6.8.0.202311291450-r")
    implementation(group = "org.zeroturnaround", name = "zt-exec", version = "1.12")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("wikiToolkitPlugin") {
            id = "org.sinytra.wiki.toolkit"
            implementationClass = "org.sinytra.wiki.toolkit.WikiToolkitPlugin"
        }
    }
}

publishing {
    repositories {
        if (System.getenv("MAVEN_URL") != null) {
            maven {
                url = uri(System.getenv("MAVEN_URL"))
                credentials {
                    username = System.getenv("MAVEN_USERNAME") ?: "not"
                    password = System.getenv("MAVEN_PASSWORD") ?: "set"
                }
            }
        }
    }
}