plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.android.library.compose")
}

android {
    namespace = "dev.mslalith.focuslauncher.features.lunarcalendar"
}

dependencies {
    implementation(libs.kotlinx.datetime)
}
