package dev.mslalith.focuslauncher.core.domain.appswithicons

import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetAppsIconPackAwareUseCase @Inject constructor(
    private val getAppsUseCase: GetAppsUseCase,
    private val generalSettingsRepo: GeneralSettingsRepo
) {
    fun appsWithIcons(
        appsFlow: Flow<List<App>>
    ): Flow<List<AppWithIcon>> = generalSettingsRepo.iconPackTypeFlow.flatMapLatest { iconPackType ->
        getAppsUseCase.appsWithIcons(
            appsFlow = appsFlow,
            iconPackType = iconPackType
        )
    }

    fun appsWithColor(
        appsFlow: Flow<List<App>>
    ): Flow<List<AppWithColor>> = generalSettingsRepo.iconPackTypeFlow.flatMapLatest { iconPackType ->
        getAppsUseCase.appsWithColor(
            appsFlow = appsFlow,
            iconPackType = iconPackType
        )
    }
}
