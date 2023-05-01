import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.project

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("focuslauncher.android.library")
                apply("focuslauncher.android.hilt")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                add(configurationName = "implementation", project(":core:model"))
                add(configurationName = "implementation", project(":core:ui"))
                add(configurationName = "implementation", project(":core:data"))
                add(configurationName = "implementation", project(":core:common"))
                add(configurationName = "implementation", project(":core:domain"))
                add(configurationName = "implementation", project(":core:resources"))

                add(configurationName = "testImplementation", project(":core:testing"))

                add(configurationName = "implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add(configurationName = "implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                add(configurationName = "implementation", libs.findLibrary("androidx.lifecycle.runtime.compose").get())
            }
        }
    }
}
