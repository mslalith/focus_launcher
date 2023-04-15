package dev.mslalith.focuslauncher.screens.launcher

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.screens.launcher.model.PackageAction
import javax.inject.Inject

@HiltViewModel
internal class LauncherViewModel @Inject constructor(
    private val launcherAppsManager: LauncherAppsManager,
    private val appDrawerRepo: AppDrawerRepo,
    private val favoritesRepo: FavoritesRepo,
    private val hiddenAppsRepo: HiddenAppsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    fun loadApps(forceLoad: Boolean = false) {
        appCoroutineDispatcher.launchInIO {
            // this will also refresh the icon cache
            val allApps = launcherAppsManager.loadAllApps()
            appDrawerRepo.apply {
                if (!forceLoad && !areAppsEmptyInDatabase()) return@launchInIO
                addApps(allApps)
            }
        }
    }

    fun onPackageAction(packageAction: PackageAction) {
        when (packageAction) {
            is PackageAction.Added -> launcherAppsManager.loadApp(packageAction.packageName)?.let { handleAppInstall(app = it) }
            is PackageAction.Removed -> handleAppUninstall(packageAction.packageName)
            is PackageAction.Updated -> Unit
        }
    }

    fun handleAppInstall(app: App) {
        appCoroutineDispatcher.launchInIO { appDrawerRepo.addApp(app) }
    }

    fun handleAppUninstall(packageName: String) {
        appCoroutineDispatcher.launchInIO {
            appDrawerRepo.getAppBy(packageName)?.let { app ->
                favoritesRepo.removeFromFavorites(app.packageName)
                hiddenAppsRepo.removeFromHiddenApps(app.packageName)
                appDrawerRepo.removeApp(app)
            }
        }
    }
}
