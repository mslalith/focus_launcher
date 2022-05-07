import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.gradle.GradleReleaseChannel
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.PosixFilePermission

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
        classpath(Libs.buildToolsKotlinxKover)
    }
}

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

// Create a task to copy Git hooks from /scripts to .git/hooks path
val installGitHooks by tasks.creating(Copy::class) {
    from(layout.projectDirectory.file("scripts/pre-push"))
    val toDir = layout.projectDirectory.dir(".git/hooks")
    val toFile = toDir.file("pre-push").asFile
    val toFilePath = Paths.get(toFile.absolutePath)
    into(toDir)

    doLast {
        val perms = Files.getPosixFilePermissions(toFilePath)
        perms.add(PosixFilePermission.OWNER_EXECUTE)
        Files.setPosixFilePermissions(toFilePath, perms)
    }
}

// Register the Git task to run at beginning
tasks.getByPath(":app:preBuild").dependsOn(installGitHooks)
