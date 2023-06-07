plugins {
    id("focuslauncher.android.library")
    id("focuslauncher.android.hilt")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.launcherapps"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:data"))

    testImplementation(project(":core:testing"))
}
