package dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps

import dev.mslalith.focuslauncher.core.model.app.App

interface LauncherAppsManager {
    fun loadAllApps(): List<App>
    fun loadApp(packageName: String): App?
    fun defaultFavoriteApps(): List<App>
}
