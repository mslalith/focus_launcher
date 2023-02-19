plugins {
    id("focuslauncher.android.library")
    id("focuslauncher.android.library.compose")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.ui"
}

dependencies {
    implementation(libs.androidx.lifecycle.viewModelCompose)
}
