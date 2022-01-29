// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Libs.buildToolsGradle)
        classpath(Libs.buildToolsKotlinGradlePlugin)

        // Firebase
        classpath(Libs.buildToolsGoogleServices)
        classpath(Libs.buildToolsCrashlyticsGradle)


        classpath(Libs.buildToolsHiltAndroidGradlePlugin)
        classpath(Libs.buildToolsProtobufGradlePlugin)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

// ktlint-gradle
plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
}
allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
