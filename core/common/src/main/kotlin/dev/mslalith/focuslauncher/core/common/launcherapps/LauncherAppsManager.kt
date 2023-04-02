package dev.mslalith.focuslauncher.core.common.launcherapps

import android.graphics.drawable.Drawable
import dev.mslalith.focuslauncher.core.model.App

interface LauncherAppsManager {
    fun loadAllApps(): List<App>
    fun loadApp(packageName: String): App?
    fun iconFor(packageName: String): Drawable
}
