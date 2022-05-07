import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.gradle.GradleReleaseChannel
import com.vanniktech.android.junit.jacoco.JunitJacocoExtension

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
        classpath(Libs.buildToolsJacocoPlugin)
        classpath(Libs.buildToolsKotlinxKover)
    }
}

apply(plugin = "com.vanniktech.android.junit.jacoco")
apply(plugin = "kover")

// ktlint-gradle
plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("com.github.ben-manes.versions") version "0.42.0"
    id("org.jetbrains.kotlinx.kover") version "0.5.0"
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

// Configure Jacoco
configure<JunitJacocoExtension> {
    jacocoVersion = "0.8.8"
    includeNoLocationClasses = true
    includeInstrumentationCoverageInMergedReport = true
    excludes = listOf(
        "**/databinding/*Binding.*",
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/*\$ViewInjector*.*",
        "**/*\$ViewBinder*.*",
        "**/Lambda$*.class",
        "**/Lambda.class",
        "**/*Lambda.class",
        "**/*Lambda*.class",
        "**/*_MembersInjector.class",
        "**/Dagger*Component*.*",
        "**/*Module_*Factory.class",
        "**/di/module/*",
        "**/*_Factory*.*",
        "**/*Module*.*",
        "**/*Dagger*.*",
        "**/*Hilt*.*",
        "**/*MapperImpl*.*",
        "**/*\$ViewInjector*.*",
        "**/*\$ViewBinder*.*",
        "**/BuildConfig.*",
        "**/*Component*.*",
        "**/*BR*.*",
        "**/Manifest*.*",
        "**/*\$Lambda$*.*",
        "**/*Companion*.*",
        "**/*Module*.*",
        "**/*Dagger*.*",
        "**/*Hilt*.*",
        "**/*MembersInjector*.*",
        "**/*_MembersInjector.class",
        "**/*_Factory*.*",
        "**/*_Provide*Factory*.*",
        "**/*Extensions*.*",

        "**/*dagger.hilt**/**.*",
        "**hilt_aggregated_deps**"
    )
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
