package dev.mslalith.focuslauncher

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *>,
) = with(commonExtension) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.findVersion("androidxComposeCompiler").get().toString()
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
