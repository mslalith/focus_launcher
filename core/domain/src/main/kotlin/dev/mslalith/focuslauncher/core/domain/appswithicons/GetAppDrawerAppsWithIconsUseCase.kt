package dev.mslalith.focuslauncher.core.domain.appswithicons

import dev.mslalith.focuslauncher.core.common.LoadingState
import dev.mslalith.focuslauncher.core.domain.appdrawer.GetAppDrawerAppsUseCase
import dev.mslalith.focuslauncher.core.domain.model.GetAppsState
import dev.mslalith.focuslauncher.core.model.AppWithIcon
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetAppDrawerAppsWithIconsUseCase @Inject constructor(
    private val getAppsStateGivenAppsUseCase: GetAppsStateGivenAppsUseCase,
    private val getAppDrawerAppsUseCase: GetAppDrawerAppsUseCase
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(searchQueryFlow: Flow<String>): Flow<LoadingState<List<AppWithIcon>>> = getAppsStateGivenAppsUseCase(
        appsFlow = getAppDrawerAppsUseCase(searchQueryFlow = searchQueryFlow)
    ).mapLatest { getAppsState ->
        when (getAppsState) {
            is GetAppsState.AppsLoaded -> LoadingState.Loading
            is GetAppsState.AppsWithIconsLoaded -> LoadingState.Loaded(value = getAppsState.appsWithIcons)
        }
    }
}
