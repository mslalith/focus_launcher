plugins {
    id("focuslauncher.android.application")
    id("focuslauncher.android.hilt")
    id("focuslauncher.android.application.compose")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.kover)
}

android {
    namespace = "dev.mslalith.focuslauncher"

    defaultConfig {
        applicationId = "dev.mslalith.focuslauncher"
        versionCode = 10
        versionName = "0.6.0"
        setProperty("archivesBaseName", "Focus-Launcher-v$versionName")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isShrinkResources = false
            enableUnitTestCoverage = true
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
}

koverReport {
    androidReports("debug") {
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
                    "dev.mslalith.focuslauncher.**.*Constants*",
//                    "dev.mslalith.focuslauncher.**.*TestTags*",
                    "dev.mslalith.focuslauncher.**.MigrationTest"
                )
                annotatedBy(
                    "androidx.compose.runtime.Composable",
                    "androidx.compose.ui.tooling.preview.Preview",
                    "dev.mslalith.focuslauncher.core.lint.kover.IgnoreInKoverReport"
                )
            }
        }
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":feature:theme"))
    implementation(project(":screens:launcher"))
    implementation(project(":screens:editfavorites"))
    implementation(project(":screens:hideapps"))
    implementation(project(":screens:currentplace"))
    implementation(project(":screens:iconpack"))
    implementation(project(":screens:about"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.profile.installer)
}

dependencies {
    kover(project(":core:common"))
    kover(project(":core:domain"))
    kover(project(":core:data"))
    kover(project(":core:ui"))
    kover(project(":core:resources"))
    kover(project(":core:launcherapps"))
    kover(project(":screens:launcher"))
    kover(project(":screens:editfavorites"))
    kover(project(":screens:hideapps"))
    kover(project(":screens:currentplace"))
    kover(project(":screens:iconpack"))
    kover(project(":screens:about"))
    kover(project(":feature:homepage"))
    kover(project(":feature:settingspage"))
    kover(project(":feature:appdrawerpage"))
    kover(project(":feature:clock24"))
    kover(project(":feature:lunarcalendar"))
    kover(project(":feature:quoteforyou"))
    kover(project(":feature:favorites"))
    kover(project(":feature:theme"))
}
