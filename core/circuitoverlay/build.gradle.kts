plugins {
    id("focuslauncher.android.library")
    id("focuslauncher.android.library.compose")
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "dev.mslalith.focuslauncher.core.circuitoverlay"
}

dependencies {
    implementation(project(":core:screens"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)

    implementation(libs.circuit.foundation)
    implementation(libs.circuit.runtime)
    implementation(libs.circuit.overlay)
}
