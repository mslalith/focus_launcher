plugins {
    id("focuslauncher.android.library")
}

android {
    namespace = "dev.mslalith.focuslauncher.core.data.test"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(project(":core:testing"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
}
