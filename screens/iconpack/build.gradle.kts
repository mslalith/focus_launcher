plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.android.library.compose")
    id("focuslauncher.android.library.compose.testing")
}

android {
    namespace = "dev.mslalith.focuslauncher.screens.iconpack"
}

dependencies {
    implementation(project(":feature:appdrawerpage"))
    testImplementation(project(":core:testing"))
}
