plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.screen.new")
}

android {
    namespace = "dev.mslalith.focuslauncher.feature.lunarcalendar"
}

dependencies {
    implementation(libs.kotlinx.datetime)
}
