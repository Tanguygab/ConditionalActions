plugins {
    kotlin("jvm") version libs.versions.kotlin
    id("maven-publish")
}

group = "io.github.tanguygab"
version = "1.4.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.bungee)
    compileOnly(libs.papi)
    compileOnly(libs.luckperms)
    compileOnly(files("../../dependencies/TAB.jar"))
}

tasks {
    processResources {
        filesMatching(listOf("plugin.yml", "bungee.yml")) {
            expand(
                "version" to rootProject.version,
                "kotlinVersion" to libs.versions.kotlin.get()
            )
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