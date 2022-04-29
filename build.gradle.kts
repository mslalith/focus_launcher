import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.gradle.GradleReleaseChannel

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
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("com.github.ben-manes.versions") version "0.42.0"
}
allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

/*
 check for dependency updates by running
 ./gradlew dependencyUpdates

 To refresh the cache (i.e. fetch the new releases/versions of the dependencies), use flag --refresh-dependencies
 */
tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    checkForGradleUpdate = false // not required to run on every build
    gradleReleaseChannel = GradleReleaseChannel.NIGHTLY.id
    outputFormatter = "plain,html"
    outputDir = "build/dependencyUpdates"
    reportfileName = "dependency_update_report"
}
