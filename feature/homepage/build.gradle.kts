plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.screen.new")
}

android {
    namespace = "dev.mslalith.focuslauncher.feature.homepage"
}

dependencies {
    implementation(project(":feature:clock24"))
    implementation(project(":feature:lunarcalendar"))
    implementation(project(":feature:quoteforyou"))
    implementation(project(":feature:favorites"))
}
