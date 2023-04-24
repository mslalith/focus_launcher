package dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.impl

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.os.Process
import android.provider.Telephony
import android.telecom.TelecomManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.model.App
import javax.inject.Inject

internal class LauncherAppsManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LauncherAppsManager {

    private val launcherApps = context.getSystemService(LauncherApps::class.java)

    override fun loadAllApps(): List<App> = buildList {
        for (launcherActivityInfo in launcherApps.getActivityList(null, Process.myUserHandle())) {
            val applicationInfo = launcherActivityInfo.applicationInfo
            add(
                App(
                    name = applicationInfo.loadLabel(context.packageManager).toString(),
                    packageName = applicationInfo.packageName,
                    isSystem = applicationInfo.isSystemApp()
                )
            )
        }
    }

    override fun loadApp(packageName: String): App? {
        val launcherActivityInfo = launcherApps.getActivityList(packageName, Process.myUserHandle()).firstOrNull() ?: return null
        return App(
            name = launcherActivityInfo.label.toString(),
            packageName = packageName,
            isSystem = launcherActivityInfo.applicationInfo.isSystemApp()
        )
    }

    override fun defaultFavoriteApps(): List<App> = listOfNotNull(defaultDialerApp(), defaultMessagingApp())

    private fun ApplicationInfo.isSystemApp() = try {
        (flags and (ApplicationInfo.FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0
    } catch (ex: PackageManager.NameNotFoundException) {
        false
    }

    private fun defaultDialerApp(): App? {
        val manager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        return manager.defaultDialerPackage?.let { loadApp(packageName = it) }
    }

    private fun defaultMessagingApp(): App? {
        val packageName: String? = Telephony.Sms.getDefaultSmsPackage(context)
        return packageName?.let { loadApp(packageName = packageName) }
    }
}
