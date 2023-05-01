package dev.mslalith.focuslauncher.core.domain.launcherapps

import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.model.App
import javax.inject.Inject

class GetDefaultFavoriteAppsUseCase @Inject constructor(
    private val launcherAppsManager: LauncherAppsManager
) {
    operator fun invoke(): List<App> = launcherAppsManager.defaultFavoriteApps()
}
