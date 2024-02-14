import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import io.sentry.android.gradle.extensions.SentryPluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import java.io.FileInputStream
import java.util.Properties

class SentryPlugin : Plugin<Project> {

    private companion object {
        private const val SENTRY_PROPERTIES_FILE = "sentry.properties"
        private const val SENTRY_DSN_ENV = "SENTRY_DSN"
        private const val SENTRY_DSN_PROPERTY = "sentry.dsn"
        private const val SENTRY_AUTH_TOKEN_PROPERTY = "sentry.auth.token"
    }

    override fun apply(target: Project) = with(target) {
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        pluginManager.apply {
            apply("com.android.application")
            apply(libs.findPlugin("sentry").get().get().pluginId)
        }

        // take from ENV to allow builds in CI
        // otherwise read from sentry.properties
        val sentryDsn = readSentryValueOf(propertyKey = SENTRY_DSN_PROPERTY, envKey = SENTRY_DSN_ENV, default = "")
        val sentryAuthToken = readSentryValueOf(SENTRY_AUTH_TOKEN_PROPERTY, default = "")

        extensions.configure<ApplicationAndroidComponentsExtension> {
            onVariants { variant ->
                variant.manifestPlaceholders.put("sentryDsn", sentryDsn)
            }
        }

        extensions.configure<SentryPluginExtension> {
            org.set("ms-m5")
            projectName.set("focus-launcher")
            authToken.set(sentryAuthToken)

            includeSourceContext.set(true)
            tracingInstrumentation.enabled.set(false)
            ignoreForVariants()
        }
    }

    private fun SentryPluginExtension.ignoreForVariants() {
//        ignoredBuildTypes.set(setOf("debug"))
//        ignoredFlavors.set( setOf("dev"))
        ignoredVariants.set(
            setOf(
                "devDebug",
                "storeDebug"
            )
        )
    }

    private fun Project.readSentryValueOf(
        propertyKey: String,
        envKey: String = propertyKey,
        default: String? = null
    ): String = providers.environmentVariable(envKey).orNull ?: readSentrySecret(key = propertyKey, default = default)

    private fun Project.readSentrySecret(
        key: String,
        default: String?
    ): String {
        val secretPropertiesFile = rootProject.file(SENTRY_PROPERTIES_FILE)
        if (secretPropertiesFile.exists().not()) {
            return default ?: error("$SENTRY_PROPERTIES_FILE file is required for GitHub configuration")
        }

        val localProperties = Properties().apply { load(FileInputStream(secretPropertiesFile)) }
        return localProperties[key]?.toString() ?: default ?: error("$key field is required in $SENTRY_PROPERTIES_FILE")
    }
}
