import com.android.build.gradle.LibraryExtension
import com.google.devtools.ksp.gradle.KspExtension
import dev.mslalith.focuslauncher.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class NewScreenConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.library")
                apply("com.google.devtools.ksp")
            }
            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(commonExtension = extension)

            extensions.configure<KspExtension> {
                arg("circuit.codegen.mode", "hilt")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add(configurationName = "implementation", project(":core:screens"))
                add(configurationName = "implementation", libs.findLibrary("circuit.foundation").get())
                add(configurationName = "implementation", libs.findLibrary("circuit-runtime").get())
                add(configurationName = "api", libs.findLibrary("circuit-codegenAnnotations").get())
                add(configurationName = "ksp", libs.findLibrary("circuit-codegen").get())
            }
        }
    }
}
