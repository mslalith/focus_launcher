plugins {
    id("focuslauncher.android.library")
    id("focuslauncher.android.library.compose")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.testing.circuit"
}

dependencies {
    api(project(":core:testing"))

    implementation(libs.circuit.foundation)
    implementation(libs.circuit.overlay)
    api(libs.circuit.test)
}
