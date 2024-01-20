package dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.impl

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.os.Process
import android.provider.Telephony
import android.telecom.TelecomManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.lint.kover.IgnoreInKoverReport
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithComponent
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@IgnoreInKoverReport
internal class LauncherAppsManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appDrawerRepo: AppDrawerRepo
) : LauncherAppsManager {

    private val launcherApps = context.getSystemService(LauncherApps::class.java)

    override suspend fun loadAllApps(): List<AppWithComponent> = buildList {
        val allApps = appDrawerRepo.allAppsFlow.firstOrNull().orEmpty()
        for (launcherActivityInfo in launcherApps.getActivityList(null, Process.myUserHandle())) {
            val applicationInfo = launcherActivityInfo.applicationInfo
            val localApp = allApps.find { it.packageName == applicationInfo.packageName }
            val appWithComponent = if (localApp != null) {
                AppWithComponent(
                    app = localApp,
                    componentName = launcherActivityInfo.componentName
                )
            } else {
                AppWithComponent(
                    app = App(
                        name = applicationInfo.loadLabel(context.packageManager).toString(),
                        packageName = applicationInfo.packageName,
                        isSystem = applicationInfo.isSystemApp()
                    ),
                    componentName = launcherActivityInfo.componentName
                )
            }
            add(appWithComponent)
        }
    }

    override suspend fun loadApp(packageName: String): AppWithComponent? {
        val localApp = appDrawerRepo.getAppBy(packageName = packageName)
        val launcherActivityInfo = launcherApps.getActivityList(packageName, Process.myUserHandle()).firstOrNull() ?: return null
        val appName = launcherActivityInfo.label.toString()

        if (localApp != null) {
            val displayName = if (localApp.name == localApp.displayName) {
                // custom display name was not set
                // update to new app name
                appName
            } else {
                // custom display name is set
                // use the existing one
                localApp.displayName
            }

            return AppWithComponent(
                app = localApp.copy(
                    name = appName,
                    displayName = displayName
                ),
                componentName = launcherActivityInfo.componentName
            )
        }

        return AppWithComponent(
            app = App(
                name = appName,
                packageName = packageName,
                isSystem = launcherActivityInfo.applicationInfo.isSystemApp()
            ),
            componentName = launcherActivityInfo.componentName
        )
    }

    override suspend fun defaultFavoriteApps(): List<AppWithComponent> = listOfNotNull(defaultDialerApp(), defaultMessagingApp())

    private fun ApplicationInfo.isSystemApp() = try {
        (flags and (ApplicationInfo.FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0
    } catch (ex: PackageManager.NameNotFoundException) {
        false
    }

    private suspend fun defaultDialerApp(): AppWithComponent? {
        val manager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        return manager.defaultDialerPackage?.let { loadApp(packageName = it) }
    }

    private suspend fun defaultMessagingApp(): AppWithComponent? {
        val packageName: String? = Telephony.Sms.getDefaultSmsPackage(context)
        return packageName?.let { loadApp(packageName = it) }
    }
}
