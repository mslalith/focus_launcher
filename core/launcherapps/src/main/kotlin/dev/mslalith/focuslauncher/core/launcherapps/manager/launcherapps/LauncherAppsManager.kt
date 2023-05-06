package dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps

import dev.mslalith.focuslauncher.core.model.app.AppWithComponent

interface LauncherAppsManager {
    fun loadAllApps(): List<AppWithComponent>
    fun loadApp(packageName: String): AppWithComponent?
    fun defaultFavoriteApps(): List<AppWithComponent>
}
