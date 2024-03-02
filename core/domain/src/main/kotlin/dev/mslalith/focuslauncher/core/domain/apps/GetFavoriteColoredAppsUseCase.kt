package dev.mslalith.focuslauncher.core.domain.apps

import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.domain.apps.core.GetAppsIconPackAwareUseCase
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetFavoriteColoredAppsUseCase @Inject internal constructor(
    private val getAppsIconPackAwareUseCase: GetAppsIconPackAwareUseCase,
    private val favoritesRepo: FavoritesRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) {
    operator fun invoke(): Flow<List<AppWithColor>> = getAppsIconPackAwareUseCase.appsWithColor(
        appsFlow = favoritesRepo.onlyFavoritesFlow
    ).flowOn(context = appCoroutineDispatcher.io)
}
