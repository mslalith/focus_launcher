package dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.test

import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.model.app.App

class TestLauncherAppsManager : LauncherAppsManager {

    private var allApps = emptyList<App>()

    override fun loadAllApps(): List<App> = allApps

    override fun loadApp(packageName: String): App = App(
        name = packageName,
        packageName = packageName,
        isSystem = false
    )

    override fun defaultFavoriteApps(): List<App> = emptyList()

    fun setAllApps(apps: List<App>) {
        allApps = apps
    }
}
