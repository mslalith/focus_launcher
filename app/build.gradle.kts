import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.protobuf")
    id("dagger.hilt.android.plugin")
    // Firebase
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = ConfigData.TARGET_SDK
    buildToolsVersion = ConfigData.BUILD_TOOLS

    defaultConfig {
        applicationId = "dev.mslalith.focuslauncher"
        minSdk = ConfigData.MIN_SDK
        targetSdk = ConfigData.TARGET_SDK
        versionCode = ConfigData.VERSION_CODE
        versionName = ConfigData.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }

        manifestPlaceholders["crashlyticsEnabled"] = true
    }

    sourceSets["main"].java.srcDir("src/main/kotlin")

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            manifestPlaceholders["crashlyticsEnabled"] = false
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            manifestPlaceholders["crashlyticsEnabled"] = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE
    }
}

dependencies {
    kotlin()
    google()
    androidx()

    compose()
    composeInterop()

    firebase()
    hilt()
    room()
    dataStore()
    accompanist()
    retrofit()
    protobuf()

    thirdPartyLibs()
    testLibs()
}

protobuf {
    protoc {
        artifact = if (project.osdetector.arch == "aarch_64") {
            "${Libs.protobufCompiler}:${project.osdetector.os}-${project.osdetector.arch}"
        } else {
            Libs.protobufCompiler
        }
    }

    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("java") {
                    option("lite")
                }
            }
            // it.builtins {
            //     id("kotlin")
            // }
        }
    }
}
