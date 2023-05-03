package dev.mslalith.focuslauncher.core.domain.appswithicons

import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.domain.appdrawer.GetAppDrawerAppsUseCase
import dev.mslalith.focuslauncher.core.domain.extensions.filterAppsWithIconsState
import dev.mslalith.focuslauncher.core.model.app.AppWithIconFavorite
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetAppDrawerAppsWithIconsUseCase @Inject internal constructor(
    private val getAppsStateGivenAppsAndIconPackTypeUseCase: GetAppsStateGivenAppsAndIconPackTypeUseCase,
    private val getAppDrawerAppsUseCase: GetAppDrawerAppsUseCase,
    private val generalSettingsRepo: GeneralSettingsRepo,
    private val favoritesRepo: FavoritesRepo
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(searchQueryFlow: Flow<String>): Flow<List<AppWithIconFavorite>> = generalSettingsRepo.iconPackTypeFlow
        .flatMapLatest { iconPackType ->
            getAppsStateGivenAppsAndIconPackTypeUseCase(
                appsFlow = getAppDrawerAppsUseCase(searchQueryFlow = searchQueryFlow),
                iconPackType = iconPackType
            ).filterAppsWithIconsState().combine(flow = favoritesRepo.onlyFavoritesFlow) { appsWithIcons, favorites ->
                appsWithIcons.map { appWithIcon ->
                    AppWithIconFavorite(
                        appWithIcon = appWithIcon,
                        isFavorite = favorites.any { it.packageName == appWithIcon.packageName }
                    )
                }
            }
        }
}
