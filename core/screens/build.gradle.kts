plugins {
    id("focuslauncher.android.library")
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "dev.mslalith.focuslauncher.core.screens"
}

dependencies {
    implementation(projects.core.model)

    implementation(libs.circuit.runtime)
}
