plugins {
    id("focuslauncher.android.library")
    id("focuslauncher.android.hilt")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.domain"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.launcherapps)
    implementation(projects.core.settings.sentry)

    implementation(libs.androidx.palette.ktx)

    testImplementation(projects.core.testing)
}
