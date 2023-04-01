package dev.mslalith.focuslauncher.screens.editfavourites

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.model.SelectedApp
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

@HiltViewModel
internal class EditFavouritesViewModel @Inject constructor(
    appDrawerRepo: AppDrawerRepo,
    private val favouritesRepo: FavoritesRepo,
    hiddenAppsRepo: HiddenAppsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    private val _showHiddenAppsInFavorites = MutableStateFlow(false)
    val showHiddenAppsInFavorites = _showHiddenAppsInFavorites.asStateFlow()

    fun shouldShowHiddenAppsInFavorites(value: Boolean) {
        _showHiddenAppsInFavorites.value = value
    }

    val favouritesStateFlow: StateFlow<List<SelectedApp>> = appDrawerRepo.allAppsFlow
        .map { it.map { app -> SelectedApp(app = app, isSelected = false) } }
        .combine(favouritesRepo.onlyFavoritesFlow) { appsList, onlyFavoritesList ->
            appsList.map { selectedApp ->
                val isSelected = onlyFavoritesList.any { it.packageName == selectedApp.app.packageName }
                selectedApp.copy(isSelected = isSelected)
            }
        }
        .combine(hiddenAppsRepo.onlyHiddenAppsFlow) { favoriteApps, onlyHiddenApps ->
            favoriteApps.map { selectedApp ->
                val isHidden = onlyHiddenApps.any { it.packageName == selectedApp.app.packageName }
                selectedApp.copy(disabled = isHidden)
            }
        }
        .combine(_showHiddenAppsInFavorites) { filteredApps, filterHidden ->
            when (filterHidden) {
                true -> filteredApps
                false -> filteredApps.filterNot { it.disabled }
            }
        }.withinScope(initialValue = emptyList())

    fun addToFavourites(app: App) {
        appCoroutineDispatcher.launchInIO { favouritesRepo.addToFavorites(app) }
    }

    fun removeFromFavorites(app: App) {
        appCoroutineDispatcher.launchInIO { favouritesRepo.removeFromFavorites(app.packageName) }
    }

    fun clearFavourites() {
        appCoroutineDispatcher.launchInIO { favouritesRepo.clearFavorites() }
    }
}
