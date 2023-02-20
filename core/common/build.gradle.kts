plugins {
    id("focuslauncher.android.library")
    id("focuslauncher.android.hilt")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.common"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines.core)
}
