plugins {
    id("focuslauncher.android.library")
    id("focuslauncher.android.hilt")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.domain"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":core:model"))
}
