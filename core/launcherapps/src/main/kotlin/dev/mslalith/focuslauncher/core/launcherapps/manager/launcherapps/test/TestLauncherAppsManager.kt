package dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.test

import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.model.App

class TestLauncherAppsManager : LauncherAppsManager {

    override fun loadAllApps(): List<App> = emptyList()

    override fun loadApp(packageName: String): App = App(
        name = packageName,
        packageName = packageName,
        isSystem = false
    )
}
