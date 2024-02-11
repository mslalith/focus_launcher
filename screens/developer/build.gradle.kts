plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.screen.new")
    id("focuslauncher.android.library.compose.testing")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.mslalith.focuslauncher.screens.developer"
}

dependencies {
    implementation(libs.kotlinx.serialization)
}
