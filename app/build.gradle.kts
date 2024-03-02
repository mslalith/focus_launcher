plugins {
    id("focuslauncher.android.application")
    id("focuslauncher.android.hilt")
    id("focuslauncher.android.application.compose")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.android.application)
    alias(libs.plugins.baselineprofile)
    id("focuslauncher.sentry")
}

android {
    namespace = "dev.mslalith.focuslauncher"

    defaultConfig {
        applicationId = "dev.mslalith.focuslauncher"
        versionCode = 15
        versionName = "0.9.0"
        setProperty("archivesBaseName", "Focus-Launcher-v$versionName")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            enableUnitTestCoverage = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }

    flavorDimensionList += "version"
    productFlavors {
        create("dev") {
            dimension = "version"
            applicationIdSuffix = ".dev"
            versionNameSuffix ="-dev"
            signingConfig = signingConfigs.getByName("debug")
        }
        create("store") {
            dimension = "version"
        }
    }

    packaging {
        resources.excludes.add("META-INF/*")
    }
}

koverReport {
    androidReports("devDebug") {
        html {
            setReportDir(layout.buildDirectory.dir("kover-report/html-report"))
        }
        filters {
            excludes {
                classes(
                    "dagger.hilt.internal.aggregatedroot.codegen.**",
                    "hilt_aggregated_deps.**",
                    "dev.mslalith.focuslauncher.**.*_Factory*",
                    "dev.mslalith.focuslauncher.**.*_Impl*",
                    "dev.mslalith.**.*Hilt*",
                    "dev.mslalith.**.*_MembersInjector",
                    "dev.mslalith.**.BuildConfig",
                    "dev.mslalith.focuslauncher.**.di.**",
                    "dev.mslalith.focuslauncher.**.model.**",
                    // Circuit
                    "dev.mslalith.focuslauncher.**.*Factory",
                    "dev.mslalith.focuslauncher.**.*FactoryModule"
                )
                annotatedBy(
                    "androidx.compose.runtime.Composable",
                    "androidx.compose.ui.tooling.preview.Preview",
                    "dagger.Module",
                    "*IgnoreInKoverReport"
                )
            }
        }
    }
}

dependencies {
    baselineProfile(projects.baselineprofile)

    implementation(projects.core.screens)
    implementation(projects.core.ui)
    implementation(projects.core.model)
    implementation(projects.core.domain)
    implementation(projects.core.settings.sentry)

    implementation(projects.feature.theme)
    implementation(projects.screens.launcher)
    implementation(projects.screens.editfavorites)
    implementation(projects.screens.hideapps)
    implementation(projects.screens.currentplace)
    implementation(projects.screens.iconpack)
    implementation(projects.screens.about)
    implementation(projects.screens.developer)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.profile.installer)
    implementation(libs.androidx.lifecycle.process)

    implementation(libs.circuit.foundation)
    implementation(libs.circuit.runtime)
    implementation(libs.circuit.overlay)

    implementation(platform(libs.sentry.bom))
    implementation(libs.sentry.android.core)
}

baselineProfile {
    // Don't build on every iteration of a full assemble.
    // Instead enable generation directly for the release build variant.
    automaticGenerationDuringBuild = false
}

dependencies {
    kover(projects.core.common)
    kover(projects.core.data)
    kover(projects.core.domain)
    kover(projects.core.launcherapps)

    kover(projects.screens.about)
    kover(projects.screens.currentplace)
    kover(projects.screens.developer)
    kover(projects.screens.editfavorites)
    kover(projects.screens.hideapps)
    kover(projects.screens.iconpack)
    kover(projects.screens.launcher)

    kover(projects.feature.appdrawerpage)
    kover(projects.feature.clock24)
    kover(projects.feature.favorites)
    kover(projects.feature.lunarcalendar)
    kover(projects.feature.quoteforyou)
    kover(projects.feature.settingspage)
    kover(projects.feature.theme)
}
