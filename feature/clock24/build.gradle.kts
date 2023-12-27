plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.screen.new")
    id("focuslauncher.android.library.compose.testing")
}

android {
    namespace = "dev.mslalith.focuslauncher.feature.clock24"
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.collections.immutable)
}
