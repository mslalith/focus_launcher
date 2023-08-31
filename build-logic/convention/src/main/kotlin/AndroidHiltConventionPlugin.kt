import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("dagger.hilt.android.plugin")
            apply("com.google.devtools.ksp")
        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        dependencies {
            add(configurationName = "implementation", libs.findLibrary("hilt.android").get())
            add(configurationName = "ksp", libs.findLibrary("hilt.compiler").get())
            add(configurationName = "ksp", libs.findLibrary("hilt.android.compiler").get())
            add(configurationName = "kspTest", libs.findLibrary("hilt.android.compiler").get())
            add(configurationName = "kspAndroidTest", libs.findLibrary("hilt.android.compiler").get())
            add(configurationName = "implementation", libs.findLibrary("hilt.android.testing").get())
        }
    }
}
