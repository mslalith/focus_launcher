plugins {
    id("focuslauncher.android.library")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.testing"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    api(libs.junit4)
    implementation(libs.androidx.test.junit)
    api(libs.kotlinx.coroutines.test)
    implementation(libs.androidx.core.testing)
    api(libs.truth)
    api(libs.turbine)
    api(libs.robolectric)
    implementation(libs.kotlinx.datetime)

    api(libs.androidx.test.runner)
    api(libs.hilt.android.testing)
}
