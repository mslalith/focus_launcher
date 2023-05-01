package dev.mslalith.focuslauncher.core.domain.appswithicons

import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.model.AppWithIcon
import dev.mslalith.focuslauncher.core.model.IconPackType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAppsWithIconsGivenIconPackTypeUseCase @Inject constructor(
    private val getAppsWithIconsGivenIconPackTypeUseCase: GetAppsWithIconsGivenIconPackTypeUseCase,
    private val appDrawerRepo: AppDrawerRepo
) {
    operator fun invoke(iconPackType: IconPackType): Flow<List<AppWithIcon>> = getAppsWithIconsGivenIconPackTypeUseCase(
        appsFlow = appDrawerRepo.allAppsFlow,
        iconPackType = iconPackType
    )
}
