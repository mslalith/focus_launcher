import com.android.build.api.dsl.ApplicationExtension
import dev.mslalith.focuslauncher.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        with(pluginManager) {
            apply("com.android.application")
            apply("org.jetbrains.kotlin.android")
            apply("focuslauncher.lint")
        }

        extensions.configure<ApplicationExtension> {
            configureKotlinAndroid(commonExtension = this)
            defaultConfig.targetSdk = libs.findVersion("androidTargetSdk").get().requiredVersion.toInt()
        }
    }
}
