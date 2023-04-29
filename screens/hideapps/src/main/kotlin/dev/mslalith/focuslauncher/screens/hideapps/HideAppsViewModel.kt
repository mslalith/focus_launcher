package dev.mslalith.focuslauncher.screens.hideapps

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.model.SelectedHiddenApp
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
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

    val hiddenAppsFlow: StateFlow<List<SelectedHiddenApp>> = appDrawerRepo.allAppsFlow
        .map { it.map { app -> SelectedHiddenApp(app = app, isSelected = false, isFavorite = false) } }
        .combine(flow = favoritesRepo.onlyFavoritesFlow) { appsList, onlyFavoritesList ->
            appsList.map { hiddenApp ->
                val isFavorite = onlyFavoritesList.any { it.packageName == hiddenApp.app.packageName }
                hiddenApp.copy(isFavorite = isFavorite)
            }
        }.combine(flow = hiddenAppsRepo.onlyHiddenAppsFlow) { appsList, onlyHiddenAppsList ->
            appsList.map { hiddenApp ->
                val isSelected = onlyHiddenAppsList.any { it.packageName == hiddenApp.app.packageName }
                hiddenApp.copy(isSelected = isSelected)
            }
        }.withinScope(initialValue = emptyList())

    fun removeFromFavorites(app: App) {
        appCoroutineDispatcher.launchInIO { favoritesRepo.removeFromFavorites(packageName = app.packageName) }
    }

    fun addToHiddenApps(app: App) {
        appCoroutineDispatcher.launchInIO { hiddenAppsRepo.addToHiddenApps(app = app) }
    }

    fun removeFromHiddenApps(app: App) {
        appCoroutineDispatcher.launchInIO { hiddenAppsRepo.removeFromHiddenApps(packageName = app.packageName) }
    }

    fun clearHiddenApps() {
        appCoroutineDispatcher.launchInIO { hiddenAppsRepo.clearHiddenApps() }
    }
}
