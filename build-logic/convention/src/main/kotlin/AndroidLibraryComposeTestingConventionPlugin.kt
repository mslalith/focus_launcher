import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeTestingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            with(extensions.getByType<LibraryExtension>()) {
                testOptions {
                    unitTests.isIncludeAndroidResources = true
                }
            }

            dependencies {
                add(configurationName = "testImplementation", libs.findLibrary("androidx.compose.ui.test").get())
                add(configurationName = "debugImplementation", libs.findLibrary("androidx.compose.ui.testManifest").get())

                add(configurationName = "implementation", project(":core:testing-compose"))
                add(configurationName = "testImplementation", project(":core:testing-compose"))
            }
        }
    }
}
