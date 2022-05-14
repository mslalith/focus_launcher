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
        classpath(Libs.buildToolsHiltAndroidGradlePlugin)
        classpath(Libs.buildToolsKotlinSerialization)
        classpath(Libs.buildToolsKotlinxKover)
    }
}

apply(from = "./buildScripts/install-git-hooks.gradle.kts")
apply(plugin = "kover")

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("com.github.ben-manes.versions") version "0.42.0"
    id("org.jetbrains.kotlinx.kover") version "0.5.1"
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

kover {
    isDisabled = false
    coverageEngine.set(kotlinx.kover.api.CoverageEngine.INTELLIJ)
}

tasks.koverMergedHtmlReport {
    isEnabled = true
    htmlReportDir.set(layout.buildDirectory.dir("kover-report/html-report"))
    excludes = listOf(
        "jdk.internal.*",
        "dagger.hilt.internal.aggregatedroot.codegen.**",
        "hilt_aggregated_deps.**",
        "dev.mslalith.focuslauncher.**.*_Factory"
    )
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
