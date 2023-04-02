package dev.mslalith.focuslauncher.core.testing

import android.content.Context
import android.graphics.drawable.Drawable
import dev.mslalith.focuslauncher.core.common.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.model.App

class TestLauncherAppsManager(
    private val context: Context
) : LauncherAppsManager {
    override fun loadAllApps(): List<App> = TestApps.all

    override fun loadApp(packageName: String): App = App(
        name = packageName,
        packageName = packageName,
        isSystem = false
    )

    override fun iconFor(packageName: String): Drawable {
        return context.getDrawable(android.R.drawable.ic_delete)!!
    }
}
