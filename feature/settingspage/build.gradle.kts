plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.android.library.compose")
}

android {
    namespace = "dev.mslalith.focuslauncher.feature.settingspage"
}

dependencies {
    implementation(project(":feature:clock24"))
    implementation(project(":feature:lunarcalendar"))
    implementation(project(":feature:quoteforyou"))

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.kotlinx.collections.immutable)
}
