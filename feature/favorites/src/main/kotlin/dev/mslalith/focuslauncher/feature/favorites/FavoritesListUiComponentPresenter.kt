package dev.mslalith.focuslauncher.feature.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.domain.apps.GetFavoriteColoredAppsUseCase
import dev.mslalith.focuslauncher.core.domain.launcherapps.GetDefaultFavoriteAppsUseCase
import dev.mslalith.focuslauncher.core.screens.FavoritesListUiComponentScreen
import dev.mslalith.focuslauncher.feature.favorites.FavoritesListUiComponentUiEvent.AddDefaultAppsIfRequired
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@CircuitInject(FavoritesListUiComponentScreen::class, SingletonComponent::class)
class FavoritesListUiComponentPresenter @Inject constructor(
    private val getDefaultFavoriteAppsUseCase: GetDefaultFavoriteAppsUseCase,
    private val getFavoriteColoredAppsUseCase: GetFavoriteColoredAppsUseCase,
    private val generalSettingsRepo: GeneralSettingsRepo,
    private val favoritesRepo: FavoritesRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<FavoritesListUiComponentState> {

    @Composable
    override fun present(): FavoritesListUiComponentState {
        val scope = rememberCoroutineScope()

        val favoritesList by produceState(initialValue = persistentListOf()) {
            getFavoriteColoredAppsUseCase()
                .flowOn(context = appCoroutineDispatcher.io)
                .collectLatest { value = it.toImmutableList() }
        }

        return FavoritesListUiComponentState(
            favoritesList = favoritesList
        ) {
            when (it) {
                AddDefaultAppsIfRequired -> scope.addDefaultAppsIfRequired()
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
}
