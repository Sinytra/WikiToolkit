plugins {
    java
    `java-gradle-plugin`
}

group = "org.sinytra"
version = "1.0-SNAPSHOT"

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

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("wikiToolkitPlugin") {
            id = "org.sinytra.wiki-toolkit"
            implementationClass = "org.sinytra.toolkit.WikiToolkitPlugin"
        }
    }
}