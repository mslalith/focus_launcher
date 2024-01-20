plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.screen.new")
    id("focuslauncher.android.library.compose.testing")
}

android {
    namespace = "dev.mslalith.focuslauncher.feature.settingspage"
}

dependencies {
    implementation(projects.feature.theme)
    implementation(projects.feature.clock24)
    implementation(projects.feature.lunarcalendar)
    implementation(projects.feature.quoteforyou)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.kotlinx.collections.immutable)
}
