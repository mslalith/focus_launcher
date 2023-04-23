plugins {
    id("focuslauncher.kotlin.library")
    id("focuslauncher.lint")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization)
    implementation(libs.suncalc)
}
