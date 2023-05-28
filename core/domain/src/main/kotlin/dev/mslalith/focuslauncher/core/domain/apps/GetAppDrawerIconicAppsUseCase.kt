package dev.mslalith.focuslauncher.core.domain.apps

import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.domain.appdrawer.GetAppDrawerAppsUseCase
import dev.mslalith.focuslauncher.core.domain.apps.core.GetAppsIconPackAwareUseCase
import dev.mslalith.focuslauncher.core.domain.extensions.toFavoriteItem
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetAppDrawerIconicAppsUseCase @Inject internal constructor(
    private val getAppsIconPackAwareUseCase: GetAppsIconPackAwareUseCase,
    private val getAppDrawerAppsUseCase: GetAppDrawerAppsUseCase,
    private val favoritesRepo: FavoritesRepo
) {
    operator fun invoke(searchQueryFlow: Flow<String>): Flow<List<AppDrawerItem>> = getAppsIconPackAwareUseCase.appsWithIcons(
        appsFlow = getAppDrawerAppsUseCase(searchQueryFlow = searchQueryFlow)
    ).combine(flow = favoritesRepo.onlyFavoritesFlow) { appsWithIcons, favorites ->
        appsWithIcons.map { appWithIcon ->
            appWithIcon.toFavoriteItem(
                isFavorite = favorites.any { it.packageName == appWithIcon.app.packageName }
            )
        }
    }
}
