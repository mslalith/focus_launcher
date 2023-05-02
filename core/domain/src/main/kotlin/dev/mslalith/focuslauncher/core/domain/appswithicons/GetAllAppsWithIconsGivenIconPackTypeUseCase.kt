package dev.mslalith.focuslauncher.core.domain.appswithicons

import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.domain.extensions.filterAppsWithIconsState
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon
import dev.mslalith.focuslauncher.core.model.IconPackType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAppsWithIconsGivenIconPackTypeUseCase @Inject internal constructor(
    private val getAppsStateGivenAppsAndIconPackTypeUseCase: GetAppsStateGivenAppsAndIconPackTypeUseCase,
    private val appDrawerRepo: AppDrawerRepo
) {
    operator fun invoke(iconPackType: IconPackType): Flow<List<AppWithIcon>> = getAppsStateGivenAppsAndIconPackTypeUseCase(
        appsFlow = appDrawerRepo.allAppsFlow,
        iconPackType = iconPackType
    ).filterAppsWithIconsState()
}
