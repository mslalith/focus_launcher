package dev.mslalith.focuslauncher.core.domain.appswithicons

import dev.mslalith.focuslauncher.core.domain.appdrawer.GetAppDrawerAppsUseCase
import dev.mslalith.focuslauncher.core.domain.extensions.filterAppsWithIconsState
import dev.mslalith.focuslauncher.core.model.AppWithIcon
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppDrawerAppsWithIconsUseCase @Inject constructor(
    private val getAppsWithIconsUseCase: GetAppsWithIconsUseCase,
    private val getAppDrawerAppsUseCase: GetAppDrawerAppsUseCase
) {
    operator fun invoke(searchQueryFlow: Flow<String>): Flow<List<AppWithIcon>> = getAppsWithIconsUseCase(
        appsFlow = getAppDrawerAppsUseCase(searchQueryFlow = searchQueryFlow)
    ).filterAppsWithIconsState()
}
