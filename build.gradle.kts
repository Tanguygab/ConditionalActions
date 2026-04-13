plugins {
    kotlin("jvm") version libs.versions.kotlin
    id("maven-publish")
    alias(libs.plugins.blossom)
}

group = "io.github.tanguygab"
version = "1.6.5"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.bungee)
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
    compileOnly(libs.papi)
    compileOnly(libs.luckperms)
    compileOnly(files("../../dependencies/TAB.jar"))
}

tasks {
    processResources {
        val props = mapOf(
            "version" to rootProject.version,
            "kotlinVersion" to libs.versions.kotlin.get()
        )
        inputs.properties(props)
        filesMatching(listOf("plugin.yml", "bungee.yml")) {
            expand(props)
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("kotlinMultiplatform") {
            from(components["kotlin"])
        }
    }
}
kotlin {
    jvmToolchain(21)
}

sourceSets {
    main {
        blossom {
            kotlinSources {
                property("version", version.toString())
            }
        }
    }
}