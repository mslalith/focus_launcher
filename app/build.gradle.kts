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
        versionCode = 8
        versionName = "0.5.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isShrinkResources = false
            isTestCoverageEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
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
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":screens:launcher"))
    implementation(project(":screens:editfavorites"))
    implementation(project(":screens:hideapps"))
    implementation(project(":screens:currentplace"))
    implementation(project(":screens:iconpack"))
    implementation(project(":screens:about"))
    implementation(project(":feature:settingspage"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.profile.installer)
}
