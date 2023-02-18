import kotlinx.kover.api.KoverTaskExtension

plugins {
    id("focuslauncher.android.library")
    id("focuslauncher.android.hilt")
    id("focuslauncher.android.room")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

apply(plugin = "kover")

android {
    namespace = "dev.mslalith.focuslauncher.data"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    testOptions {
        unitTests.all {
            it.extensions.configure(KoverTaskExtension::class) {
                isDisabled.set(it.name != "testDebugUnitTest")
            }
        }
    }
}

dependencies {
    testImplementation(project(mapOf("path" to ":androidTest-shared")))

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.dataStore.core)
    implementation(libs.androidx.dataStore.preferences)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.serialization)

    implementation(libs.kotlinx.datetime)
    implementation(libs.suncalc)

    testImplementation(libs.junit4)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core.ktx)
}
