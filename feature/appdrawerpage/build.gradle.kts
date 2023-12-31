plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.screen.new")
}

android {
    namespace = "dev.mslalith.focuslauncher.feature.appdrawerpage"
}

dependencies {
    implementation(libs.kotlinx.collections.immutable)
}
