package dev.mslalith.focuslauncher.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.model.SelectedApp
import dev.mslalith.focuslauncher.extensions.appDrawerApps
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class AppsViewModel @Inject constructor(
    private val appDrawerRepo: AppDrawerRepo,
    private val favoritesRepo: FavoritesRepo,
    private val hiddenAppsRepo: HiddenAppsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    /**
     * Search Apps
     */
    private val _searchAppStateFlow = MutableStateFlow("")
    val searchAppStateFlow: StateFlow<String>
        get() = _searchAppStateFlow

    fun searchAppQuery(query: String) {
        _searchAppStateFlow.value = query
    }

    val isSearchQueryEmpty: Flow<Boolean>
        get() = _searchAppStateFlow.map { it.isEmpty() }

    /**
     * Show Hidden Apps In Favorites
     */
    private val _showHiddenAppsInFavorites = MutableStateFlow(false)
    val showHiddenAppsInFavorites = _showHiddenAppsInFavorites.asStateFlow()

    fun shouldShowHiddenAppsInFavorites(value: Boolean) {
        _showHiddenAppsInFavorites.value = value
    }

    /**
     * All's types of READ-ONLY Flow's
     */
    val appDrawerAppsStateFlow = appDrawerAppsFlow.withinScope(emptyList())
    val favoritesStateFlow = favoritesFlow.withinScope(emptyList())
    val onlyFavoritesStateFlow = appDrawerRepo.allAppsFlow
        .combine(favoritesRepo.onlyFavoritesFlow) { allApps, onlyFavorites ->
            allApps.filter { app ->
                onlyFavorites.any { app.packageName == it.packageName }
            }
        }.withinScope(emptyList())
    val hiddenAppsStateFlow = hiddenAppsFlow.withinScope(emptyList())

    private val appDrawerAppsFlow: Flow<List<App>>
        get() = appDrawerRepo.allAppsFlow.combine(hiddenAppsRepo.onlyHiddenAppsFlow) { allApps, hiddenApps ->
            allApps - hiddenApps.toSet()
        }.combine(_searchAppStateFlow) { filteredApps, query ->
            when {
                query.isNotEmpty() -> filteredApps.filter {
                    it.name.startsWith(
                        prefix = query,
                        ignoreCase = true
                    )
                }
                else -> filteredApps
            }
        }

    private val favoritesFlow: Flow<List<SelectedApp>>
        get() = appDrawerRepo.allAppsFlow
            .map { it.map { app -> SelectedApp(app, isSelected = false) } }
            .combine(favoritesRepo.onlyFavoritesFlow) { appsList, onlyFavoritesList ->
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
            }

    private val hiddenAppsFlow: Flow<List<SelectedApp>>
        get() = appDrawerRepo.allAppsFlow
            .map { it.map { app -> SelectedApp(app = app, isSelected = false) } }
            .combine(hiddenAppsRepo.onlyHiddenAppsFlow) { appsList, onlyHiddenAppsList ->
                appsList.map { selectedApp ->
                    val isSelected =
                        onlyHiddenAppsList.any { it.packageName == selectedApp.app.packageName }
                    selectedApp.copy(isSelected = isSelected)
                }
            }

    fun setAppsIfCacheEmpty(context: Context, checkCache: Boolean = true) {
        launch {
            appDrawerRepo.apply {
                if (checkCache) {
                    if (areAppsEmptyInDatabase()) {
                        addApps(context.appDrawerApps)
                    }
                    return@apply
                }
                addApps(context.appDrawerApps)
            }
        }
    }

    fun updateDisplayName(app: App, displayName: String) {
        launch { appDrawerRepo.updateDisplayName(app, displayName) }
    }

    /**
     * Favorites
     */
    suspend fun isFavorite(packageName: String) = favoritesRepo.isFavorite(packageName)

    fun addToFavorites(app: App) {
        launch { favoritesRepo.addToFavorites(app) }
    }

    fun reorderFavorite(app: App, withApp: App, onReordered: () -> Unit) {
        if (app.packageName == withApp.packageName) {
            onReordered()
            return
        }
        launch {
            favoritesRepo.reorderFavorite(app, withApp)
            withContext(appCoroutineDispatcher.main) {
                onReordered()
            }
        }
    }

    fun removeFromFavorites(app: App) {
        launch { favoritesRepo.removeFromFavorites(app.packageName) }
    }

    fun clearFavorites() {
        launch { favoritesRepo.clearFavorites() }
    }

    /**
     * Hidden Apps
     */
    fun addToHiddenApps(app: App) {
        launch { hiddenAppsRepo.addToHiddenApps(app) }
    }

    fun removeFromHiddenApps(app: App) {
        launch { hiddenAppsRepo.removeFromHiddenApps(app.packageName) }
    }

    fun clearHiddenApps() {
        launch { hiddenAppsRepo.clearHiddenApps() }
    }

    /**
     * App Install/Uninstall
     */
    fun handleAppInstall(app: App) {
        launch { appDrawerRepo.addApp(app) }
    }

    fun handleAppUninstall(packageName: String) {
        launch {
            appDrawerRepo.getAppBy(packageName)?.let { app ->
                // calling suspend repo methods to execute sequentially
                favoritesRepo.removeFromFavorites(app.packageName)
                hiddenAppsRepo.removeFromHiddenApps(app.packageName)
                appDrawerRepo.removeApp(app)
            }
        }
    }

    private fun launch(
        run: suspend () -> Unit
    ) = viewModelScope.launch(appCoroutineDispatcher.io) { run() }

    private fun <T> Flow<T>.withinScope(
        initialValue: T
    ) = stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = initialValue
    )
}
