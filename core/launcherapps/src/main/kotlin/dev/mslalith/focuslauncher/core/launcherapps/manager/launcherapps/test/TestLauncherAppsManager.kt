package dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.test

import android.graphics.drawable.Drawable
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.model.App

class TestLauncherAppsManager : LauncherAppsManager {

    override fun loadAllApps(): List<App> = emptyList()

    override fun loadApp(packageName: String): App = App(
        name = packageName,
        packageName = packageName,
        isSystem = false
    )

    override fun iconFor(packageName: String): Drawable {
        return Drawable.createFromPath("M11,2l2,0l0,5l8,0l0,3l-8,0l0,4l5,0l0,3l-5,0l0,5l-2,0l0,-5l-5,0l0,-3l5,0l0,-4l-8,0l0,-3l8,0z")!!
    }
}
