plugins {
    id("focuslauncher.android.library")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.testing"
}

dependencies {
    implementation(project(":core:model"))
    api(project(":core:launcherapps"))

    api(libs.junit4)
    implementation(libs.androidx.test.junit)
    api(libs.kotlinx.coroutines.test)
    implementation(libs.androidx.core.testing)
    api(libs.truth)
    api(libs.turbine)
    api(libs.mockk)
    api(libs.robolectric)
    implementation(libs.kotlinx.datetime)

    api(libs.androidx.test.runner)
    api(libs.hilt.android.testing)

    api(libs.ktor.client.mock)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.serialization)
}
