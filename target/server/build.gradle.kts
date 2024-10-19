import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

group = "br.com.firstsoft"
version = "0.0.1"

dependencies {
    implementation(libs.jna)
    implementation(libs.kotlinx.serialization)

    implementation(compose.desktop.currentOs)
    implementation(libs.compose.material.icons)

    implementation(projects.core.common)
    implementation(projects.core.native)
}

sourceSets {
    main {
        java {
            srcDir("src/main/kotlin")
        }
    }
}

compose.desktop {
    application {
        mainClass = "br.com.firstsoft.target.server.ServerMainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Msi)
            packageName = "PCMonitoR"
            packageVersion = "0.0.1"
        }
    }
}