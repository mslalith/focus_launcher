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
    ":benchmark",
    ":core:common",
    ":core:model",
    ":core:screens",
    ":core:circuitoverlay",
    ":core:domain",
    ":core:data",
    ":core:data-test",
    ":core:ui",
    ":core:resources",
    ":core:launcherapps",
    ":core:lint",
    ":core:testing",
    ":core:testing-compose",
    ":core:testing-circuit",
    ":screens:launcher",
    ":screens:editfavorites",
    ":screens:hideapps",
    ":screens:currentplace",
    ":screens:iconpack",
    ":screens:about",
    ":feature:homepage",
    ":feature:settingspage",
    ":feature:appdrawerpage",
    ":feature:clock24",
    ":feature:lunarcalendar",
    ":feature:quoteforyou",
    ":feature:favorites",
    ":feature:theme"
)
