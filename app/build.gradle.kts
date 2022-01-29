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
    compileSdk = 31
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "dev.mslalith.focuslauncher"
        minSdk =21
        targetSdk = 31
        versionCode = 1
        versionName = "0.1"

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
    // Kotlin Libraries
    implementation(Libs.coreKtx)
    implementation(Libs.kotlinxDateTime)


    // Androidx & Compose Libraries
    implementation(Libs.googleMaterial)
    implementation(Libs.composeMaterial)
    implementation(Libs.composeUi)
    implementation(Libs.composeUiTooling)
    implementation(Libs.lifecycleRuntimeKtx)
    implementation(Libs.activityCompose)
    implementation(Libs.constraintLayoutCompose)
    implementation(Libs.navigationCompose)


    // Google & Android Libraries
    implementation(Libs.googleProtobufJavalite)
    implementation(Libs.googlePlayCoreKtx)
    implementation(Libs.paletteKtx)


    // Architecture Components
    implementation(Libs.lifecycleViewModelCompose)
    implementation(Libs.dataStore)
    implementation(Libs.dataStorePreferences)


    // Firebase
    implementation(platform(Libs.firebaseBom))
    implementation(Libs.firebaseCrashlyticsKtx)
    implementation(Libs.firebaseAnalyticsKtx)


    // Hilt DI Libraries
    implementation(Libs.hiltAndroid)
    kapt(Libs.hiltAndroidCompiler)

    implementation(Libs.hiltLifecycleViewModel)
    kapt(Libs.hiltCompiler)
    implementation(Libs.hiltNavigationCompose)


    // Room
    implementation(Libs.roomKtx)
    implementation(Libs.roomRuntime)
    kapt(Libs.roomCompiler)


    // Accompanist
    implementation(Libs.accompanistPager)
    implementation(Libs.accompanistSystemUiController)
    implementation(Libs.accompanistInsets)
    implementation(Libs.accompanistFlowLayout)


    // Retrofit
    implementation(Libs.retrofit)
    implementation(Libs.retrofitGsonConverter)


    // 3rd Party Libraries
    implementation(Libs.thirdSunCalc)


    // Test Libraries
    testImplementation(Libs.testJUnit)
    androidTestImplementation(Libs.testAndroidXJUnit)
    androidTestImplementation(Libs.testAndroidXEspresso)
    androidTestImplementation(Libs.testComposeJUnit)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.14.0:osx-x86_64"
    }

    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("java") {
                    option("lite")
                }
            }
        }
    }
}
