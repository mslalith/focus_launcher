import kotlinx.kover.api.KoverTaskExtension

plugins {
    id("focuslauncher.android.application")
    id("focuslauncher.android.hilt")
    id("focuslauncher.android.application.compose")
    alias(libs.plugins.ksp)
}

apply(plugin = "kover")

android {
    namespace = "dev.mslalith.focuslauncher"

    defaultConfig {
        applicationId = "dev.mslalith.focuslauncher"
        versionCode = 6
        versionName = "0.3.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets["main"].java.srcDir("src/main/kotlin")

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isShrinkResources = false
            isTestCoverageEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    testOptions {
        unitTests.all {
            it.extensions.configure(KoverTaskExtension::class) {
                isDisabled.set(it.name != "testDebugUnitTest")
            }
        }
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":core:model"))
    testImplementation(project(mapOf("path" to ":androidTest-shared")))

    implementation(libs.kotlinx.datetime)

    implementation(libs.google.material)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.palette.ktx)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewModelCompose)

    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.dataStore.core)
    implementation(libs.androidx.dataStore.preferences)

    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.accompanist.systemuicontroller)

    testImplementation(libs.junit4)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.room.runtime)
}
