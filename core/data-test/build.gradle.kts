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
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(project(":core:testing"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
}
