package dev.mslalith.focuslauncher

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) = with(commonExtension) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    buildFeatures {
        compose = true
    }

    extensions.configure<ComposeCompilerGradlePluginExtension> {
        fun Provider<String>.onlyIfTrue() = flatMap { provider { it.takeIf(String::toBoolean) } }
        fun Provider<*>.relativeToRootProject(dir: String) = flatMap {
            rootProject.layout.buildDirectory.dir(projectDir.toRelativeString(rootDir))
        }.map { it.dir(dir) }

        project.providers.gradleProperty("enableComposeCompilerMetrics").onlyIfTrue()
            .relativeToRootProject("compose-metrics")
            .let(metricsDestination::set)

        project.providers.gradleProperty("enableComposeCompilerReports").onlyIfTrue()
            .relativeToRootProject("compose-reports")
            .let(reportsDestination::set)

//        stabilityConfigurationFile.set(rootProject.layout.projectDirectory.file("compose_compiler_config.conf"))
//        enableStrongSkippingMode.set(true)
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
