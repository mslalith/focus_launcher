package dev.mslalith.focuslauncher.core.common.launcherapps.impl

import android.content.Context
import android.content.pm.LauncherApps
import android.graphics.drawable.Drawable
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mslalith.focuslauncher.core.common.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.model.App
import javax.inject.Inject

internal class LauncherAppsManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LauncherAppsManager {
    private val launcherApps = context.getSystemService(LauncherApps::class.java)

    private val iconCache = hashMapOf<String, Drawable>()

    override fun loadAllApps(): List<App> {
        val appsList = mutableListOf<App>()

        val userHandle = launcherApps.profiles.firstOrNull() ?: return appsList

        for (launcherActivityInfo in launcherApps.getActivityList(null, userHandle)) {
            val packageName = launcherActivityInfo.applicationInfo.packageName
            val app = App(
                name = launcherActivityInfo.label.toString(),
                packageName = packageName,
                isSystem = false,
            )

            iconCache[packageName] = context.packageManager.getApplicationIcon(packageName)
            appsList.add(app)
        }

        return appsList
    }

    override fun loadApp(packageName: String): App? {
        val userHandle = launcherApps.profiles.firstOrNull() ?: return null
        val launcherActivityInfo = launcherApps.getActivityList(packageName, userHandle).firstOrNull() ?: return null
        val app = App(
            name = launcherActivityInfo.label.toString(),
            packageName = packageName,
            isSystem = false,
        )

        iconCache[packageName] = launcherActivityInfo.getIcon(context.resources.configuration.densityDpi)

        return app
    }

    override fun iconFor(packageName: String): Drawable = iconCache.getOrPut(packageName) {
        context.packageManager.getApplicationIcon(packageName)
    }
}
