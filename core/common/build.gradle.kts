plugins {
    id("focuslauncher.android.library")
    id("focuslauncher.android.hilt")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.common"
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines.core)
}
