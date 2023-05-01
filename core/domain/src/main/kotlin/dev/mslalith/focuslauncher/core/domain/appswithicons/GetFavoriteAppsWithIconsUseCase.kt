package dev.mslalith.focuslauncher.core.domain.appswithicons

import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.domain.extensions.filterAppsWithIconsState
import dev.mslalith.focuslauncher.core.model.AppWithIcon
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteAppsWithIconsUseCase @Inject constructor(
    private val getAppsStateGivenAppsUseCase: GetAppsStateGivenAppsUseCase,
    private val favoritesRepo: FavoritesRepo
) {
    operator fun invoke(): Flow<List<AppWithIcon>> = getAppsStateGivenAppsUseCase(
        appsFlow = favoritesRepo.onlyFavoritesFlow
    ).filterAppsWithIconsState()
}
