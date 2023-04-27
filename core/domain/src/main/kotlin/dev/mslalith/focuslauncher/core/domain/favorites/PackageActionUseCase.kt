package dev.mslalith.focuslauncher.core.domain.favorites

import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.model.PackageAction
import javax.inject.Inject

class PackageActionUseCase @Inject constructor(
    private val launcherAppsManager: LauncherAppsManager,
    private val appDrawerRepo: AppDrawerRepo,
    private val favoritesRepo: FavoritesRepo,
    private val hiddenAppsRepo: HiddenAppsRepo
) {
    suspend operator fun invoke(packageAction: PackageAction) = onPackageAction(packageAction = packageAction)

    private suspend fun onPackageAction(packageAction: PackageAction) {
        when (packageAction) {
            is PackageAction.Added -> launcherAppsManager.loadApp(packageName = packageAction.packageName)?.let { handleAppInstall(app = it) }
            is PackageAction.Removed -> handleAppUninstall(packageName = packageAction.packageName)
            is PackageAction.Updated -> Unit
        }
    }

    internal suspend fun handleAppInstall(app: App) {
        appDrawerRepo.addApp(app = app)
    }

    internal suspend fun handleAppUninstall(packageName: String) {
        appDrawerRepo.getAppBy(packageName = packageName)?.let { app ->
            favoritesRepo.removeFromFavorites(packageName = app.packageName)
            hiddenAppsRepo.removeFromHiddenApps(packageName = app.packageName)
            appDrawerRepo.removeApp(app = app)
        }
    }
}
