package dev.mslalith.focuslauncher.screens.hideapps

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.screens.hideapps.model.HiddenApp
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

@HiltViewModel
internal class HideAppsViewModel @Inject constructor(
    appDrawerRepo: AppDrawerRepo,
    private val favoritesRepo: FavoritesRepo,
    private val hiddenAppsRepo: HiddenAppsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    val hiddenAppsFlow: StateFlow<List<HiddenApp>> = appDrawerRepo.allAppsFlow
        .map { it.map { app -> HiddenApp(app = app, isSelected = false, isFavorite = false) } }
        .combine(favoritesRepo.onlyFavoritesFlow) { appsList, onlyFavoritesList ->
            appsList.map { hiddenApp ->
                val isFavorite = onlyFavoritesList.any { it.packageName == hiddenApp.app.packageName }
                hiddenApp.copy(isFavorite = isFavorite)
            }
        }.combine(hiddenAppsRepo.onlyHiddenAppsFlow) { appsList, onlyHiddenAppsList ->
            appsList.map { hiddenApp ->
                val isSelected = onlyHiddenAppsList.any { it.packageName == hiddenApp.app.packageName }
                hiddenApp.copy(isSelected = isSelected)
            }
        }.withinScope(initialValue = emptyList())

    fun removeFromFavorites(app: App) {
        appCoroutineDispatcher.launchInIO { favoritesRepo.removeFromFavorites(app.packageName) }
    }

    fun addToHiddenApps(app: App) {
        appCoroutineDispatcher.launchInIO { hiddenAppsRepo.addToHiddenApps(app) }
    }

    fun removeFromHiddenApps(app: App) {
        appCoroutineDispatcher.launchInIO { hiddenAppsRepo.removeFromHiddenApps(app.packageName) }
    }

    fun clearHiddenApps() {
        appCoroutineDispatcher.launchInIO { hiddenAppsRepo.clearHiddenApps() }
    }
}
