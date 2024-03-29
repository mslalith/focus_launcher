plugins {
    id("focuslauncher.android.library")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.testing.circuit"

    packaging {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {
    api(projects.core.testing)

    implementation(libs.circuit.foundation)
    api(libs.circuit.test)
}
