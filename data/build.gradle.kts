plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "dev.mslalith.focuslauncher.data"
    compileSdk = ConfigData.TARGET_SDK

    defaultConfig {
        minSdk = ConfigData.MIN_SDK
        targetSdk = ConfigData.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isTestCoverageEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        release {
            isMinifyEnabled = false
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
}

dependencies {
    androidTestImplementation(project(mapOf("path" to ":androidTest-shared")))

    androidxCoreKtx()
    hiltAndroid()
    room()
    dataStore()

    implementation(Libs.retrofitGsonConverter)
    implementation(Libs.kotlinxDateTime)
    implementation(Libs.googlePlayCoreKtx)
    implementation(Libs.thirdSunCalc)

    junit(includeAndroid = true)
    truth(includeAndroid = true)
    kotlinxCoroutinesTest(includeAndroid = true)
    turbine(includeAndroid = true)
}
