plugins {
    id("focuslauncher.android.library")
    id("focuslauncher.android.hilt")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.launcherapps"
}

dependencies {
    implementation(project(":core:model"))

    testImplementation(project(":core:testing"))
}
