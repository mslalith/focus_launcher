package dev.mslalith.focuslauncher.feature.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.domain.apps.GetFavoriteColoredAppsUseCase
import dev.mslalith.focuslauncher.core.domain.launcherapps.GetDefaultFavoriteAppsUseCase
import dev.mslalith.focuslauncher.core.domain.theme.GetThemeUseCase
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_THEME
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.screens.FavoritesListUiComponentScreen
import dev.mslalith.focuslauncher.feature.favorites.FavoritesListUiComponentUiEvent.AddDefaultAppsIfRequired
import dev.mslalith.focuslauncher.feature.favorites.FavoritesListUiComponentUiEvent.RemoveFromFavorites
import dev.mslalith.focuslauncher.feature.favorites.FavoritesListUiComponentUiEvent.ReorderFavorite
import dev.mslalith.focuslauncher.feature.favorites.FavoritesListUiComponentUiEvent.UpdateFavoritesContextMode
import dev.mslalith.focuslauncher.feature.favorites.model.FavoritesContextMode
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@CircuitInject(FavoritesListUiComponentScreen::class, SingletonComponent::class)
class FavoritesListUiComponentPresenter @Inject constructor(
    private val getDefaultFavoriteAppsUseCase: GetDefaultFavoriteAppsUseCase,
    private val getFavoriteColoredAppsUseCase: GetFavoriteColoredAppsUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val generalSettingsRepo: GeneralSettingsRepo,
    private val favoritesRepo: FavoritesRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<FavoritesListUiComponentState> {

    private var favoritesContextualMode by mutableStateOf<FavoritesContextMode>(value = FavoritesContextMode.Closed)

    @Composable
    override fun present(): FavoritesListUiComponentState {
        val scope = rememberCoroutineScope()

        val favoritesList by produceState(initialValue = persistentListOf()) {
            getFavoriteColoredAppsUseCase()
                .flowOn(context = appCoroutineDispatcher.io)
                .collectLatest { value = it.toImmutableList() }
        }

        val currentTheme by produceState(initialValue = DEFAULT_THEME) {
            getThemeUseCase()
                .flowOn(context = appCoroutineDispatcher.io)
                .collectLatest { value = it }
        }

        return FavoritesListUiComponentState(
            favoritesList = favoritesList,
            favoritesContextualMode = favoritesContextualMode,
            currentTheme = currentTheme
        ) {
            when (it) {
                AddDefaultAppsIfRequired -> scope.addDefaultAppsIfRequired()
                is RemoveFromFavorites -> scope.removeFromFavorites(app = it.app)
                is ReorderFavorite -> scope.reorderFavorite(app = it.app, withApp = it.withApp, onReordered = it.onReordered)
                is UpdateFavoritesContextMode -> favoritesContextualMode = it.mode
            }
        }
    }

    private fun CoroutineScope.addDefaultAppsIfRequired() {
        launch(appCoroutineDispatcher.io) {
            if (generalSettingsRepo.firstRunFlow.first()) {
                generalSettingsRepo.overrideFirstRun()
                favoritesRepo.addToFavorites(apps = getDefaultFavoriteAppsUseCase())
            }
        }
    }

    private fun CoroutineScope.reorderFavorite(app: App, withApp: App, onReordered: () -> Unit) {
        if (app.packageName == withApp.packageName) {
            onReordered()
            return
        }
        launch(appCoroutineDispatcher.io) {
            favoritesRepo.reorderFavorite(app = app, withApp = withApp)
            withContext(appCoroutineDispatcher.main) {
                onReordered()
            }
        }
    }

    private fun CoroutineScope.removeFromFavorites(app: App) {
        launch(appCoroutineDispatcher.io) {
            favoritesRepo.removeFromFavorites(packageName = app.packageName)
        }
    }
}
