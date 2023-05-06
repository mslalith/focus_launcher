package dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.test

import android.content.ComponentName
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithComponent

class TestLauncherAppsManager : LauncherAppsManager {

    private var allApps = emptyList<AppWithComponent>()

    override fun loadAllApps(): List<AppWithComponent> = allApps

    override fun loadApp(packageName: String): AppWithComponent = AppWithComponent(
        app = App(
            name = packageName,
            packageName = packageName,
            isSystem = false
        ),
        componentName = ComponentName(packageName, "")
    )

    override fun defaultFavoriteApps(): List<AppWithComponent> = emptyList()

    fun setAllApps(apps: List<AppWithComponent>) {
        allApps = apps
    }
}
