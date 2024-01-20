package dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.test

import android.content.ComponentName
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithComponent

typealias LoadAppBehaviour = (AppWithComponent) -> AppWithComponent

class TestLauncherAppsManager : LauncherAppsManager {

    private var allApps = emptyList<AppWithComponent>()

    private val loadAppForPackageBehaviours = mutableMapOf<String, LoadAppBehaviour>()

    override suspend fun loadAllApps(): List<AppWithComponent> = allApps

    override suspend fun loadApp(packageName: String): AppWithComponent {
        val defaultAppWithComponent = loadAppInternal(packageName = packageName)
        return loadAppForPackageBehaviours[packageName]?.invoke(defaultAppWithComponent) ?: defaultAppWithComponent
    }

    override suspend fun defaultFavoriteApps(): List<AppWithComponent> = emptyList()

    fun setAllApps(apps: List<AppWithComponent>) {
        allApps = apps
    }

    fun loadAppForPackage(packageName: String, behaviour: LoadAppBehaviour) {
        loadAppForPackageBehaviours[packageName] = behaviour
    }

    private fun loadAppInternal(packageName: String): AppWithComponent = AppWithComponent(
        app = App(
            name = packageName,
            packageName = packageName,
            isSystem = false
        ),
        componentName = ComponentName(packageName, "")
    )
}
