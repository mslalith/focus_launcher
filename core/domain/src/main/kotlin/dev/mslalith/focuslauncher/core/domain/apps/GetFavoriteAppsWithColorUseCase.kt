package dev.mslalith.focuslauncher.core.domain.apps

import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteAppsWithColorUseCase @Inject internal constructor(
    private val getAppsIconPackAwareUseCase: GetAppsIconPackAwareUseCase,
    private val favoritesRepo: FavoritesRepo
) {
    operator fun invoke(): Flow<List<AppWithColor>> = getAppsIconPackAwareUseCase.appsWithColor(
        appsFlow = favoritesRepo.onlyFavoritesFlow
    )
}
