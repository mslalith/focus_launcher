plugins {
    id("focuslauncher.android.library")
    id("focuslauncher.android.library.compose")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.testing.compose"
}

dependencies {
    implementation(libs.androidx.compose.ui.test)
}
