pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
    }
}

buildCache {
    local {
        isEnabled = true
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}

rootProject.name = "Focus Launcher"
include(
    ":app",
    ":data",
    ":androidTest-shared",
    ":core:common",
    ":core:model",
    ":core:domain",
    ":core:ui",
    ":feature:clock24",
    ":feature:lunarcalendar",
    ":feature:quoteforyou"
)
