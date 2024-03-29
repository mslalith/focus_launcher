plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.screen.new")
    id("focuslauncher.android.library.compose.testing")
}

android {
    namespace = "dev.mslalith.focuslauncher.screens.editfavorites"
}

dependencies {
    implementation(projects.core.ui)

    implementation(libs.kotlinx.collections.immutable)
}
