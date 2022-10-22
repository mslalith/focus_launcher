import kotlinx.kover.api.KoverTaskExtension

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
}

apply(plugin = "kover")

android {
    namespace = "dev.mslalith.focuslauncher.data"
    compileSdk = ConfigData.TARGET_SDK

    defaultConfig {
        minSdk = ConfigData.MIN_SDK
        targetSdk = ConfigData.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
            arg("room.incremental", "true")
            arg("room.expandProjection", "true")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isTestCoverageEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    lint {
        error.add("VisibleForTests")
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
    testImplementation(project(mapOf("path" to ":androidTest-shared")))

    androidxCoreKtx()
    hiltAndroid()
    room()
    dataStore()
    ktorClient()
    playInAppUpdate()

    implementation(Libs.kotlinxDateTime)
    implementation(Libs.thirdSunCalc)

    junit(includeAndroid = false)
    truth(includeAndroid = false)
    kotlinxCoroutinesTest(includeAndroid = false)
    turbine(includeAndroid = false)
    robolectric()
}
