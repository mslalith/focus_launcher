package dev.mslalith.focuslauncher.core.domain.appswithicons

import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.model.AppWithIcon
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetAppsWithIconsUseCase @Inject constructor(
    private val getAppsWithIconsGivenIconPackTypeUseCase: GetAppsWithIconsGivenIconPackTypeUseCase,
    private val generalSettingsRepo: GeneralSettingsRepo
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(appsFlow: Flow<List<App>>): Flow<List<AppWithIcon>> = generalSettingsRepo.iconPackTypeFlow
        .flatMapLatest {
            getAppsWithIconsGivenIconPackTypeUseCase(appsFlow = appsFlow, iconPackType = it)
        }
}
