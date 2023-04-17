plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.android.library.compose")
}

android {
    namespace = "dev.mslalith.focuslauncher.feature.appdrawerpage"
}

dependencies {
    implementation(project(":core:launcherapps"))
}
