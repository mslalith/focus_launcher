plugins {
    id("focuslauncher.android.library")
    id("focuslauncher.android.hilt")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.domain"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:model"))
}
