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
import dev.mslalith.focuslauncher.core.lint.kover.IgnoreInKoverReport
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithComponent
import javax.inject.Inject

@IgnoreInKoverReport
internal class LauncherAppsManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LauncherAppsManager {

    private val launcherApps = context.getSystemService(LauncherApps::class.java)

    override fun loadAllApps(): List<AppWithComponent> = buildList {
        for (launcherActivityInfo in launcherApps.getActivityList(null, Process.myUserHandle())) {
            val applicationInfo = launcherActivityInfo.applicationInfo
            add(
                AppWithComponent(
                    app = App(
                        name = applicationInfo.loadLabel(context.packageManager).toString(),
                        packageName = applicationInfo.packageName,
                        isSystem = applicationInfo.isSystemApp()
                    ),
                    componentName = launcherActivityInfo.componentName
                )
            )
        }
    }

    override fun loadApp(packageName: String): AppWithComponent? {
        val launcherActivityInfo = launcherApps.getActivityList(packageName, Process.myUserHandle()).firstOrNull() ?: return null
        return AppWithComponent(
            app = App(
                name = launcherActivityInfo.label.toString(),
                packageName = packageName,
                isSystem = launcherActivityInfo.applicationInfo.isSystemApp()
            ),
            componentName = launcherActivityInfo.componentName
        )
    }

    override fun defaultFavoriteApps(): List<AppWithComponent> = listOfNotNull(defaultDialerApp(), defaultMessagingApp())

    private fun ApplicationInfo.isSystemApp() = try {
        (flags and (ApplicationInfo.FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0
    } catch (ex: PackageManager.NameNotFoundException) {
        false
    }

    private fun defaultDialerApp(): AppWithComponent? {
        val manager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        return manager.defaultDialerPackage?.let(::loadApp)
    }

    private fun defaultMessagingApp(): AppWithComponent? {
        val packageName: String? = Telephony.Sms.getDefaultSmsPackage(context)
        return packageName?.let(::loadApp)
    }
}
