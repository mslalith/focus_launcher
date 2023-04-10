import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

apply(from = "./buildScripts/install-git-hooks.gradle.kts")
apply(plugin = "kover")

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.android) apply false
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    additionalEditorconfigFile.set(file(".editorconfig"))
    reporters {
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.JSON)
    }
}

detekt {
    toolVersion = libs.versions.detekt.get()
    config = files("config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}

kover {
    isDisabled.set(false)
    engine.set(kotlinx.kover.api.DefaultIntellijEngine)
}

koverMerged {
    enable()
    htmlReport {
        reportDir.set(layout.buildDirectory.dir("kover-report/html-report"))
        filters {
            projects {
                excludes += listOf(":core:testing")
            }
            classes {
                excludes += listOf(
                    "jdk.internal.*",
                    "dagger.hilt.internal.aggregatedroot.codegen.**",
                    "hilt_aggregated_deps.**",
                    "dev.mslalith.focuslauncher.**.*_Factory"
                )
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
