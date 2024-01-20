plugins {
    id("focuslauncher.android.library")
    id("focuslauncher.android.hilt")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.launcherapps"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.data)

    testImplementation(projects.core.testing)
}
