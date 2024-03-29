package dev.mslalith.focuslauncher.core.domain

import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.PackageAction
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PackageActionUseCase @Inject constructor(
    private val launcherAppsManager: LauncherAppsManager,
    private val appDrawerRepo: AppDrawerRepo,
    private val favoritesRepo: FavoritesRepo,
    private val hiddenAppsRepo: HiddenAppsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) {
    suspend operator fun invoke(packageAction: PackageAction) = onPackageAction(packageAction = packageAction)

    private suspend fun onPackageAction(packageAction: PackageAction) = withContext(context = appCoroutineDispatcher.io) {
        when (packageAction) {
            is PackageAction.Added -> launcherAppsManager.loadApp(packageName = packageAction.packageName)?.let { handleAppInstall(app = it.app) }
            is PackageAction.Updated -> launcherAppsManager.loadApp(packageName = packageAction.packageName)?.let { handleAppInstall(app = it.app) }
            is PackageAction.Removed -> handleAppUninstall(packageName = packageAction.packageName)
        }
    }

    private suspend fun handleAppInstall(app: App) {
        appDrawerRepo.addApp(app = app)
    }

    private suspend fun handleAppUninstall(packageName: String) {
        appDrawerRepo.getAppBy(packageName = packageName)?.let { app ->
            favoritesRepo.removeFromFavorites(packageName = app.packageName)
            hiddenAppsRepo.removeFromHiddenApps(packageName = app.packageName)
            appDrawerRepo.removeApp(app = app)
        }
    }
}
