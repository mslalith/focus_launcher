package dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.impl

import android.content.Context
import android.content.pm.LauncherApps
import android.os.Process
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mslalith.focuslauncher.core.launcherapps.manager.icons.IconManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.impl.IconPackManagerImpl
import dev.mslalith.focuslauncher.core.model.App
import javax.inject.Inject

internal class LauncherAppsManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val iconManager: IconManager
) : LauncherAppsManager {

    private val launcherApps = context.getSystemService(LauncherApps::class.java)

    override fun loadAllApps(): List<App> {
        val appsList = mutableListOf<App>()

        for (launcherActivityInfo in launcherApps.getActivityList(null, Process.myUserHandle())) {
            val packageName = launcherActivityInfo.applicationInfo.packageName
            val app = App(
                name = launcherActivityInfo.applicationInfo.loadLabel(context.packageManager).toString(),
                packageName = packageName,
                isSystem = false,
            )

            appsList.add(app)
            iconManager.addToCache(
                packageName = packageName,
                drawable = context.packageManager.getApplicationIcon(packageName)
            )
        }

        return appsList
    }

    override fun loadApp(packageName: String): App? {
        val launcherActivityInfo = launcherApps.getActivityList(packageName, Process.myUserHandle()).firstOrNull() ?: return null
        val app = App(
            name = launcherActivityInfo.label.toString(),
            packageName = packageName,
            isSystem = false,
        )

        iconManager.addToCache(
            packageName = packageName,
            drawable = launcherActivityInfo.getIcon(context.resources.configuration.densityDpi)
        )

        return app
    }
}
