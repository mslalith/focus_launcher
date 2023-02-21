plugins {
    id("focuslauncher.android.library")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.testing"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(libs.junit4)
    implementation(libs.androidx.test.junit)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.androidx.core.testing)
    implementation(libs.turbine)

    implementation(libs.androidx.dataStore.core)
    implementation(libs.androidx.dataStore.preferences)
}
