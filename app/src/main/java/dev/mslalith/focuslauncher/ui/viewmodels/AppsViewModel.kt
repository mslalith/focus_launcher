package dev.mslalith.focuslauncher.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.data.model.App
import dev.mslalith.focuslauncher.data.model.SelectedApp
import dev.mslalith.focuslauncher.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.extensions.appDrawerApps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class AppsViewModel @Inject constructor(
    private val appDrawerRepo: AppDrawerRepo,
    private val favoritesRepo: FavoritesRepo,
    private val hiddenAppsRepo: HiddenAppsRepo,
) : ViewModel() {

    /**
     * Search Apps
     */
    private val searchAppFlow = MutableStateFlow("")
    fun searchAppQuery(query: String) {
        searchAppFlow.value = query
    }

    val isSearchQueryEmpty: Flow<Boolean>
        get() = searchAppFlow.map { it.isEmpty() }

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
    val onlyFavoritesStateFlow = favoritesRepo.onlyFavoritesFlow.withinScope(emptyList())
    val hiddenAppsStateFlow = hiddenAppsFlow.withinScope(emptyList())

    private val appDrawerAppsFlow: Flow<List<App>>
        get() = appDrawerRepo.allAppsFlow.combine(hiddenAppsRepo.onlyHiddenAppsFlow) { allApps, hiddenApps ->
            allApps - hiddenApps.toSet()
        }.combine(searchAppFlow) { filteredApps, query ->
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
        viewModelScope.launch {
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
            withContext(Dispatchers.Main) {
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
                removeFromFavorites(app)
                removeFromHiddenApps(app)
                appDrawerRepo.removeApp(app)
            }
        }
    }

    private fun launch(
        coroutineContext: CoroutineContext = Dispatchers.IO,
        run: suspend () -> Unit,
    ) = viewModelScope.launch(coroutineContext) { run() }

    private fun <T> Flow<T>.withinScope(
        initialValue: T,
    ) = stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = initialValue
    )
}
