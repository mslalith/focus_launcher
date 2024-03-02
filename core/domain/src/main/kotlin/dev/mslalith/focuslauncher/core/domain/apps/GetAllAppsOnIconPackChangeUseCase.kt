package dev.mslalith.focuslauncher.core.domain.apps

import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.domain.apps.core.GetAppsUseCase
import dev.mslalith.focuslauncher.core.domain.extensions.toFavoriteItem
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllAppsOnIconPackChangeUseCase @Inject internal constructor(
    private val getAppsUseCase: GetAppsUseCase,
    private val appDrawerRepo: AppDrawerRepo,
    private val favoritesRepo: FavoritesRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) {
    operator fun invoke(iconPackType: IconPackType): Flow<List<AppDrawerItem>> = getAppsUseCase.appsWithIcons(
        appsFlow = appDrawerRepo.allAppsFlow,
        iconPackType = iconPackType
    ).combine(flow = favoritesRepo.onlyFavoritesFlow) { appsWithIcons, favorites ->
        appsWithIcons.map { appWithIcon ->
            appWithIcon.toFavoriteItem(
                isFavorite = favorites.any { it.packageName == appWithIcon.app.packageName }
            )
        }
    }.flowOn(context = appCoroutineDispatcher.io)
}
