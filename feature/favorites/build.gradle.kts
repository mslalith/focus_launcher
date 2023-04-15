plugins {
    id("focuslauncher.android.feature")
    id("focuslauncher.android.library.compose")
}

android {
    namespace = "dev.mslalith.focuslauncher.feature.favorites"
}

dependencies {
    implementation(libs.androidx.palette.ktx)
    implementation(libs.accompanist.flowlayout)
    testImplementation(project(":core:testing"))
}
