plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.android.library.compose")
    id("focuslauncher.android.library.compose.testing")
}

android {
    namespace = "dev.mslalith.focuslauncher.screens.hideapps"
}

dependencies {
    implementation(libs.kotlinx.collections.immutable)
}
