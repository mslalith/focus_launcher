apply(from = "./buildScripts/install-git-hooks.gradle.kts")

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlinx.kover) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.android) apply false
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
}

detekt {
    toolVersion = libs.versions.detekt.get()
    config.from(files("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}

setupTestLogging()

fun Project.setupTestLogging() {
    for (sub in subprojects) {
        sub.tasks.withType<Test> {
            testLogging {
                events("standardOut")
                showStandardStreams = true
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
