plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.android.library.compose")
    id("focuslauncher.android.library.compose.testing")
}

android {
    namespace = "dev.mslalith.focuslauncher.screens.launcher"
}

dependencies {
    implementation(project(":core:launcherapps"))
    implementation(project(":feature:homepage"))
    implementation(project(":feature:settingspage"))
    implementation(project(":feature:appdrawerpage"))

    testImplementation(project(":core:testing"))
}
