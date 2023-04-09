plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.android.library.compose")
    id("focuslauncher.android.library.compose.testing")
}

android {
    namespace = "dev.mslalith.focuslauncher.screens.currentplace"
}

dependencies {
    implementation(libs.osmdroid)
    testImplementation(project(":core:testing"))
}
