plugins {
    id("focuslauncher.android.library")
    id("focuslauncher.lint")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.mslalith.focuslauncher.core.model"
}

dependencies {
    implementation(project(":core:resources"))

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization)
    implementation(libs.androidx.annotation)
    implementation(libs.suncalc)
}
