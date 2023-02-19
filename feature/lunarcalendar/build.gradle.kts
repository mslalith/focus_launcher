plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.android.library.compose")
}

android {
    namespace = "dev.mslalith.focuslauncher.feature.lunarcalendar"
}

dependencies {
    implementation(libs.kotlinx.datetime)
}
