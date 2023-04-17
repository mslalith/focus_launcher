package dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.impl

import android.content.Context
import android.content.pm.LauncherApps
import android.os.Process
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.model.App
import javax.inject.Inject

internal class LauncherAppsManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LauncherAppsManager {

    private val launcherApps = context.getSystemService(LauncherApps::class.java)

    override fun loadAllApps(): List<App> = buildList {
        for (launcherActivityInfo in launcherApps.getActivityList(null, Process.myUserHandle())) {
            val packageName = launcherActivityInfo.applicationInfo.packageName
            add(
                App(
                    name = launcherActivityInfo.applicationInfo.loadLabel(context.packageManager).toString(),
                    packageName = packageName,
                    isSystem = false,
                )
            )
        }
    }

    override fun loadApp(packageName: String): App? {
        val launcherActivityInfo = launcherApps.getActivityList(packageName, Process.myUserHandle()).firstOrNull() ?: return null
        return App(
            name = launcherActivityInfo.label.toString(),
            packageName = packageName,
            isSystem = false,
        )
    }
}
