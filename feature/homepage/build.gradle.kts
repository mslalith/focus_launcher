plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.screen.new")
}

android {
    namespace = "dev.mslalith.focuslauncher.feature.homepage"
}

dependencies {
    implementation(projects.feature.clock24)
    implementation(projects.feature.lunarcalendar)
    implementation(projects.feature.quoteforyou)
    implementation(projects.feature.favorites)
}
