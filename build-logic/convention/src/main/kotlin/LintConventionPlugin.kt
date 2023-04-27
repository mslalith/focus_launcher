import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.project

class LintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("io.gitlab.arturbosch.detekt")

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        dependencies {
            add(configurationName = "implementation", project(":core:lint"))
            add(configurationName = "detektPlugins", libs.findLibrary("detekt.formatting").get())
        }
    }
}
