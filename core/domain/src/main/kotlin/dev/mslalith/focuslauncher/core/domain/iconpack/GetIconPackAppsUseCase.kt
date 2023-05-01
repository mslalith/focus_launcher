package dev.mslalith.focuslauncher.core.domain.iconpack

import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import dev.mslalith.focuslauncher.core.model.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetIconPackAppsUseCase @Inject constructor(
    private val iconPackManager: IconPackManager,
    private val appDrawerRepo: AppDrawerRepo
) {
    operator fun invoke(): Flow<List<App>> = appDrawerRepo.allAppsFlow
        .combine(flow = iconPackManager.iconPacksFlow) { allApps, iconPacks ->
            allApps.filter { app ->
                iconPacks.any { it.packageName == app.packageName }
            }
        }
}
