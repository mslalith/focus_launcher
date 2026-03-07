import com.android.build.api.dsl.LibraryExtension
import dev.mslalith.focuslauncher.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        with(pluginManager) {
            apply("com.android.library")
            apply("focuslauncher.lint")
            apply(libs.findPlugin("kotlinx-kover").get().get().pluginId)
        }

        extensions.configure<LibraryExtension> {
            configureKotlinAndroid(commonExtension = this)

            // Robolectric SDK 36 requires Java 21; pin to 35 while CI/builds run on Java 17.
            testOptions.unitTests.all {
                it.systemProperty("robolectric.enabledSdks", "35")
            }
        }
    }
}
