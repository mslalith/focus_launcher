package dev.mslalith.focuslauncher

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) = with(commonExtension) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.findVersion("androidxComposeCompiler").get().toString()
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs += buildComposeMetricsParameters()
        }
    }

    dependencies {
        val bom = libs.findLibrary("androidx.compose.bom").get()
        add(configurationName = "implementation", platform(bom))
        add(configurationName = "androidTestImplementation", platform(bom))

        add(configurationName = "implementation", libs.findLibrary("androidx.compose.material3").get())
        add(configurationName = "implementation", libs.findLibrary("androidx.compose.ui").get())
        add(configurationName = "implementation", libs.findLibrary("androidx.compose.ui.tooling").get())
    }
}

private fun Project.buildComposeMetricsParameters(): List<String> {
    val metricParameters = mutableListOf<String>()
    val enableMetricsProvider = project.providers.gradleProperty("enableComposeCompilerMetrics")
    val enableMetrics = (enableMetricsProvider.orNull == "true")
    if (enableMetrics) {
        val metricsFolder = File(project.layout.buildDirectory.asFile.get(), "compose-metrics")
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + metricsFolder.absolutePath
        )
    }

    val enableReportsProvider = project.providers.gradleProperty("enableComposeCompilerReports")
    val enableReports = (enableReportsProvider.orNull == "true")
    if (enableReports) {
        val reportsFolder = File(project.layout.buildDirectory.asFile.get(), "compose-reports")
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + reportsFolder.absolutePath
        )
    }
    return metricParameters.toList()
}
