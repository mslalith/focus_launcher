plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.android.library.compose")
}

android {
    namespace = "dev.mslalith.focuslauncher.feature.homepage"
}

dependencies {
    implementation(project(":feature:clock24"))
    implementation(project(":feature:lunarcalendar"))
    implementation(project(":feature:quoteforyou"))
    implementation(libs.androidx.palette.ktx)
    implementation(libs.accompanist.flowlayout)
}
