package dev.mslalith.focuslauncher.core.domain.launcherapps

import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import javax.inject.Inject

class LoadAllAppsUseCase @Inject constructor(
    private val launcherAppsManager: LauncherAppsManager,
    private val appDrawerRepo: AppDrawerRepo
) {
    suspend operator fun invoke(forceLoad: Boolean = false) {
        appDrawerRepo.apply {
            if (!forceLoad && !areAppsEmptyInDatabase()) return
            addApps(apps = launcherAppsManager.loadAllApps().map { it.app })
        }
    }
}
