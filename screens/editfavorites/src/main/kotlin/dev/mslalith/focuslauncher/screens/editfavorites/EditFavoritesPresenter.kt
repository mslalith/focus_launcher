package dev.mslalith.focuslauncher.screens.editfavorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import dev.mslalith.focuslauncher.core.model.app.SelectedApp
import dev.mslalith.focuslauncher.core.screens.EditFavoritesScreen
import dev.mslalith.focuslauncher.screens.editfavorites.EditFavoritesUiEvent.AddToFavorites
import dev.mslalith.focuslauncher.screens.editfavorites.EditFavoritesUiEvent.ClearFavorites
import dev.mslalith.focuslauncher.screens.editfavorites.EditFavoritesUiEvent.GoBack
import dev.mslalith.focuslauncher.screens.editfavorites.EditFavoritesUiEvent.RemoveFromFavorites
import dev.mslalith.focuslauncher.screens.editfavorites.EditFavoritesUiEvent.ToggleShowingHiddenApps
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EditFavoritesPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    appDrawerRepo: AppDrawerRepo,
    private val favoritesRepo: FavoritesRepo,
    hiddenAppsRepo: HiddenAppsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<EditFavoritesState> {

    @CircuitInject(EditFavoritesScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): EditFavoritesPresenter
    }

    private var showHiddenApps by mutableStateOf(value = false)

    private val allFavoritesFlow: Flow<ImmutableList<SelectedApp>> = appDrawerRepo.allAppsFlow
        .map { it.map { app -> SelectedApp(app = app, isSelected = false) } }
        .combine(flow = favoritesRepo.onlyFavoritesFlow) { appsList, onlyFavoritesList ->
            appsList.map { selectedApp ->
                val isSelected = onlyFavoritesList.any { it.packageName == selectedApp.app.packageName }
                selectedApp.copy(isSelected = isSelected)
            }
        }
        .combine(flow = hiddenAppsRepo.onlyHiddenAppsFlow) { favoriteApps, onlyHiddenApps ->
            favoriteApps.map { selectedApp ->
                val isHidden = onlyHiddenApps.any { it.packageName == selectedApp.app.packageName }
                selectedApp.copy(disabled = isHidden)
            }.toImmutableList()
        }

    @Composable
    override fun present(): EditFavoritesState {
        val scope = rememberCoroutineScope()

        val allFavorites by allFavoritesFlow.collectAsState(initial = persistentListOf())
        val favoriteApps by remember {
            derivedStateOf {
                when (showHiddenApps) {
                    true -> allFavorites
                    false -> allFavorites.filterNot { it.disabled }
                }.toImmutableList()
            }
        }

        return EditFavoritesState(
            favoriteApps = favoriteApps,
            showHiddenApps = showHiddenApps
        ) {
            when (it) {
                is AddToFavorites -> scope.addToFavorites(app = it.app)
                is RemoveFromFavorites -> scope.removeFromFavorites(app = it.app)
                ClearFavorites -> scope.clearFavorites()
                ToggleShowingHiddenApps -> showHiddenApps = !showHiddenApps
                GoBack -> navigator.pop()
            }
        }
    }

    private fun CoroutineScope.addToFavorites(app: App) {
        launch(appCoroutineDispatcher.io) { favoritesRepo.addToFavorites(app = app) }
    }

    private fun CoroutineScope.removeFromFavorites(app: App) {
        launch(appCoroutineDispatcher.io) { favoritesRepo.removeFromFavorites(packageName = app.packageName) }
    }

    private fun CoroutineScope.clearFavorites() {
        launch(appCoroutineDispatcher.io) { favoritesRepo.clearFavorites() }
    }
}
