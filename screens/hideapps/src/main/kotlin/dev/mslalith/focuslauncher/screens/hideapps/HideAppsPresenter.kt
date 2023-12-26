package dev.mslalith.focuslauncher.screens.hideapps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.SelectedHiddenApp
import dev.mslalith.focuslauncher.core.screens.HideAppsScreen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HideAppsPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    appDrawerRepo: AppDrawerRepo,
    private val favoritesRepo: FavoritesRepo,
    private val hiddenAppsRepo: HiddenAppsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<HideAppsState> {

    @CircuitInject(HideAppsScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): HideAppsPresenter
    }

    private val allHiddenAppsFlow: Flow<ImmutableList<SelectedHiddenApp>> = appDrawerRepo.allAppsFlow
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
            }.toImmutableList()
        }

    @Composable
    override fun present(): HideAppsState {
        val scope = rememberCoroutineScope()

        val hiddenApps by allHiddenAppsFlow.collectAsState(initial = persistentListOf())

        return HideAppsState(
            hiddenApps = hiddenApps
        ) {
            when (it) {
                is HideAppsUiEvent.AddToHiddenApps -> scope.addToHiddenApps(app = it.app)
                is HideAppsUiEvent.RemoveFromHiddenApps -> scope.removeFromHiddenApps(app = it.app)
                HideAppsUiEvent.ClearHiddenApps -> scope.clearHiddenApps()
                is HideAppsUiEvent.RemoveFromFavorites -> scope.removeFromFavorites(app = it.app)
                HideAppsUiEvent.GoBack -> navigator.pop()
            }
        }
    }

    private fun CoroutineScope.addToHiddenApps(app: App) {
        launch(appCoroutineDispatcher.io) { hiddenAppsRepo.addToHiddenApps(app = app) }
    }

    private fun CoroutineScope.removeFromHiddenApps(app: App) {
        launch(appCoroutineDispatcher.io) { hiddenAppsRepo.removeFromHiddenApps(packageName = app.packageName) }
    }

    private fun CoroutineScope.clearHiddenApps() {
        launch(appCoroutineDispatcher.io) { hiddenAppsRepo.clearHiddenApps() }
    }

    private fun CoroutineScope.removeFromFavorites(app: App) {
        launch(appCoroutineDispatcher.io) { favoritesRepo.removeFromFavorites(packageName = app.packageName) }
    }
}
