package dev.mslalith.focuslauncher.core.testing

import android.content.ComponentName
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithComponent

object TestApps {
    val Chrome = App(name = "Chrome", packageName = "com.android.chrome", isSystem = true)
    val Youtube = App(name = "Youtube", packageName = "com.android.youtube", isSystem = false)
    val Phone = App(name = "Phone", packageName = "com.android.phone", isSystem = true)

    val all = listOf(Chrome, Phone, Youtube)
}

fun List<App>.toAppsWithComponents(): List<AppWithComponent> = map { app ->
    AppWithComponent(
        app = app,
        componentName = ComponentName(app.packageName, "")
    )
}

fun List<App>.toPackageNamed(): List<App> = map(App::toPackageNamed)

fun App.toPackageNamed(): App = copy(
    name = packageName,
    displayName = packageName
)

fun List<App>.disableAsSystem(): List<App> = map(App::disableAsSystem)

fun App.disableAsSystem(): App = copy(isSystem = false)
