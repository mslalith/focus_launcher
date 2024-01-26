plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.screen.new")
}

android {
    namespace = "dev.mslalith.focuslauncher.feature.theme"
}

dependencies {
    implementation(libs.kotlinx.collections.immutable)
}
