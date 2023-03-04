package dev.mslalith.focuslauncher.feature.homepage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.feature.homepage.model.FavoritesContextMode
import dev.mslalith.focuslauncher.feature.homepage.model.HomePageState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

@HiltViewModel
internal class HomePageViewModel @Inject constructor(
    private val generalSettingsRepo: GeneralSettingsRepo,
    private val favoritesRepo: FavoritesRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    private val _favoritesContextualMode: MutableStateFlow<FavoritesContextMode> = MutableStateFlow(value = FavoritesContextMode.Closed)

    private val defaultHomePageState = HomePageState(
        isPullDownNotificationShadeEnabled = DEFAULT_NOTIFICATION_SHADE,
        favoritesContextualMode = _favoritesContextualMode.value,
        favoritesList = emptyList()
    )

    val homePageState = flowOf(defaultHomePageState)
        .combine(generalSettingsRepo.notificationShadeFlow) { state, isPullDownNotificationShadeEnabled ->
            state.copy(isPullDownNotificationShadeEnabled = isPullDownNotificationShadeEnabled)
        }.combine(favoritesRepo.onlyFavoritesFlow) { state, favorites ->
            state.copy(favoritesList = favorites)
        }.combine(_favoritesContextualMode) { state, favoritesContextualMode ->
            state.copy(favoritesContextualMode = favoritesContextualMode)
        }.withinScope(initialValue = defaultHomePageState)

    private val _showMoonCalendarDetailsDialogStateFlow = MutableStateFlow(false)
    val showMoonCalendarDetailsDialogStateFlow: StateFlow<Boolean>
        get() = _showMoonCalendarDetailsDialogStateFlow

    fun showMoonCalendarDetailsDialog() {
        _showMoonCalendarDetailsDialogStateFlow.value = true
    }

    fun hideMoonCalendarDetailsDialog() {
        _showMoonCalendarDetailsDialogStateFlow.value = false
    }

    fun addDefaultAppsIfRequired(defaultApps: List<App>) {
        appCoroutineDispatcher.launchInIO {
            val isFirstRun = generalSettingsRepo.firstRunFlow.first()
            if (isFirstRun) {
                generalSettingsRepo.overrideFirstRun()
                defaultApps.forEach { addToFavorites(it) }
            }
        }
    }

    fun addToFavorites(app: App) {
        appCoroutineDispatcher.launchInIO {
            favoritesRepo.addToFavorites(app)
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
}
