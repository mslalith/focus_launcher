package dev.mslalith.focuslauncher.core.domain.appdrawer

import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetAppDrawerAppsUseCase @Inject constructor(
    private val appDrawerRepo: AppDrawerRepo,
    private val hiddenAppsRepo: HiddenAppsRepo
) {
    operator fun invoke(searchQueryFlow: Flow<String>): Flow<List<App>> = appDrawerRepo.allAppsFlow
        .combine(flow = hiddenAppsRepo.onlyHiddenAppsFlow) { allApps, hiddenApps ->
            allApps - hiddenApps.toSet()
        }.combine(flow = searchQueryFlow) { filteredApps, query ->
            when {
                query.isNotEmpty() -> filteredApps.filter {
                    it.name.startsWith(
                        prefix = query,
                        ignoreCase = true
                    )
                }
                else -> filteredApps
            }
        }
}
