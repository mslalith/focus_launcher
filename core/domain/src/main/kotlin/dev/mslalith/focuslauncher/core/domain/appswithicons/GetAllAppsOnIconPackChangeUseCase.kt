package dev.mslalith.focuslauncher.core.domain.appswithicons

import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.domain.extensions.filterAppsWithIconsState
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.AppWithIconFavorite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetAllAppsOnIconPackChangeUseCase @Inject internal constructor(
    private val getAppsStateGivenAppsAndIconPackTypeUseCase: GetAppsStateGivenAppsAndIconPackTypeUseCase,
    private val appDrawerRepo: AppDrawerRepo,
    private val favoritesRepo: FavoritesRepo
) {
    operator fun invoke(iconPackType: IconPackType): Flow<List<AppWithIconFavorite>> = getAppsStateGivenAppsAndIconPackTypeUseCase(
        appsFlow = appDrawerRepo.allAppsFlow,
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
