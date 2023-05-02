package dev.mslalith.focuslauncher.core.domain.appswithicons

import dev.mslalith.focuslauncher.core.domain.appdrawer.GetAppDrawerAppsUseCase
import dev.mslalith.focuslauncher.core.domain.extensions.filterAppsWithIconsState
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppDrawerAppsWithIconsUseCase @Inject internal constructor(
    private val getAppsStateGivenAppsUseCase: GetAppsStateGivenAppsUseCase,
    private val getAppDrawerAppsUseCase: GetAppDrawerAppsUseCase
) {
    operator fun invoke(searchQueryFlow: Flow<String>): Flow<List<AppWithIcon>> = getAppsStateGivenAppsUseCase(
        appsFlow = getAppDrawerAppsUseCase(searchQueryFlow = searchQueryFlow)
    ).filterAppsWithIconsState()
}
