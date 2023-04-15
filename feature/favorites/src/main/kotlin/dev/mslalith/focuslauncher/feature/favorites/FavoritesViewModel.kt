package dev.mslalith.focuslauncher.feature.favorites

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon
import dev.mslalith.focuslauncher.feature.favorites.model.FavoritesContextMode
import dev.mslalith.focuslauncher.feature.favorites.model.FavoritesState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

@HiltViewModel
internal class FavoritesViewModel @Inject constructor(
    private val launcherAppsManager: LauncherAppsManager,
    private val generalSettingsRepo: GeneralSettingsRepo,
    private val favoritesRepo: FavoritesRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    private val _favoritesContextualMode: MutableStateFlow<FavoritesContextMode> = MutableStateFlow(value = FavoritesContextMode.Closed)

    private val defaultFavoritesState = FavoritesState(
        favoritesContextualMode = _favoritesContextualMode.value,
        favoritesList = emptyList()
    )

    val favoritesState = flowOf(value = defaultFavoritesState)
        .combine(favoritesRepo.onlyFavoritesFlow) { state, favorites ->
            state.copy(favoritesList = favorites.toWithIcons())
        }.combine(_favoritesContextualMode) { state, favoritesContextualMode ->
            state.copy(favoritesContextualMode = favoritesContextualMode)
        }.withinScope(initialValue = defaultFavoritesState)

    fun addDefaultAppsIfRequired(defaultApps: List<App>) {
        appCoroutineDispatcher.launchInIO {
            val isFirstRun = generalSettingsRepo.firstRunFlow.first()
            if (isFirstRun) {
                generalSettingsRepo.overrideFirstRun()
                defaultApps.forEach { favoritesRepo.addToFavorites(it) }
            }
        }
    }

    fun reorderFavorite(app: App, withApp: App, onReordered: () -> Unit) {
        if (app.packageName == withApp.packageName) {
            onReordered()
            return
        }
        appCoroutineDispatcher.launchInIO {
            favoritesRepo.reorderFavorite(app, withApp)
            withContext(appCoroutineDispatcher.main) {
                onReordered()
            }
        }
    }

    fun removeFromFavorites(app: App) {
        appCoroutineDispatcher.launchInIO {
            favoritesRepo.removeFromFavorites(app.packageName)
        }
    }

    fun isInContextualMode(): Boolean = _favoritesContextualMode.value != FavoritesContextMode.Closed

    fun changeFavoritesContextMode(mode: FavoritesContextMode) {
        _favoritesContextualMode.value = mode
    }

    fun isReordering(): Boolean = when (_favoritesContextualMode.value) {
        FavoritesContextMode.Reorder, is FavoritesContextMode.ReorderPickPosition -> true
        else -> false
    }

    fun isAppAboutToReorder(app: App): Boolean = if (_favoritesContextualMode.value is FavoritesContextMode.ReorderPickPosition) {
        (_favoritesContextualMode.value as FavoritesContextMode.ReorderPickPosition).app.packageName != app.packageName
    } else {
        false
    }

    fun hideContextualMode() {
        _favoritesContextualMode.value = FavoritesContextMode.Closed
    }

    private fun List<App>.toWithIcons(): List<AppWithIcon> = map { app ->
        AppWithIcon(
            name = app.name,
            displayName = app.displayName,
            packageName = app.packageName,
            icon = launcherAppsManager.iconFor(app.packageName),
            isSystem = app.isSystem
        )
    }
}
