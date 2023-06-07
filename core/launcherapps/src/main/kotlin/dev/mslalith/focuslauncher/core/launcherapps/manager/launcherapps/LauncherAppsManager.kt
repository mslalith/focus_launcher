package dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps

import dev.mslalith.focuslauncher.core.model.app.AppWithComponent

interface LauncherAppsManager {
    suspend fun loadAllApps(): List<AppWithComponent>
    suspend fun loadApp(packageName: String): AppWithComponent?
    suspend fun defaultFavoriteApps(): List<AppWithComponent>
}
