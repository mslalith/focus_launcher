plugins {
    id("focuslauncher.android.library")
    id("focuslauncher.android.hilt")
    id("focuslauncher.android.room")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.mslalith.focuslauncher.core.data"

    defaultConfig {
        testInstrumentationRunner = "dev.mslalith.focuslauncher.core.testing.HiltTestRunner"
    }
    sourceSets {
        getByName("test").assets.srcDir("$projectDir/schemas")
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.dataStore.core)
    implementation(libs.androidx.dataStore.preferences)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.serialization)

    implementation(libs.kotlinx.datetime)
    implementation(libs.suncalc)

    testImplementation(project(":core:testing"))
    testImplementation(libs.room.testing)
}
