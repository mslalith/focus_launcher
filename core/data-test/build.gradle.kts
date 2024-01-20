plugins {
    id("focuslauncher.android.library")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.data.test"

    packaging {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.data)
    implementation(projects.core.common)
    implementation(projects.core.testing)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
}
