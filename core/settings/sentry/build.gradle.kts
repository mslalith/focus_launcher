plugins {
    id("focuslauncher.android.library")
    id("focuslauncher.android.hilt")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.settings.sentry"
}

dependencies {
    implementation(projects.core.data)

    implementation(platform(libs.sentry.bom))
    implementation(libs.sentry.android.core)
}
