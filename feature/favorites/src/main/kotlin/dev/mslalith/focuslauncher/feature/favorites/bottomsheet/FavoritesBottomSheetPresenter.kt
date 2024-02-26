package dev.mslalith.focuslauncher.feature.favorites.bottomsheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.domain.apps.GetFavoriteColoredAppsUseCase
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import dev.mslalith.focuslauncher.core.screens.FavoritesBottomSheetScreen
import dev.mslalith.focuslauncher.feature.favorites.bottomsheet.FavoritesBottomSheetUiEvent.Move
import dev.mslalith.focuslauncher.feature.favorites.bottomsheet.FavoritesBottomSheetUiEvent.Remove
import dev.mslalith.focuslauncher.feature.favorites.bottomsheet.FavoritesBottomSheetUiEvent.Save
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesBottomSheetPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val favoritesRepo: FavoritesRepo,
    private val getFavoriteColoredAppsUseCase: GetFavoriteColoredAppsUseCase,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<FavoritesBottomSheetState> {

    @CircuitInject(FavoritesBottomSheetScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): FavoritesBottomSheetPresenter
    }

    @Composable
    override fun present(): FavoritesBottomSheetState {
        val scope = rememberCoroutineScope()
        val favoritesList = remember { mutableStateListOf<AppWithColor>() }

        LaunchedEffect(key1 = Unit) {
            val favorites = getFavoriteColoredAppsUseCase().first { it.isNotEmpty() }
            favoritesList.clear()
            favoritesList.addAll(favorites)
        }

        return FavoritesBottomSheetState(
            favoritesList = favoritesList.toImmutableList()
        ) {
            when (it) {
                is Move -> favoritesList.move(fromIndex = it.fromIndex, toIndex = it.toIndex)
                is Remove -> favoritesList -= it.appWithColor
                is Save -> scope.saveFavorites(favorites = favoritesList)
            }
        }
    }

    private fun CoroutineScope.saveFavorites(favorites: List<AppWithColor>) {
        launch(appCoroutineDispatcher.io) {
            favoritesRepo.clearFavorites()
            favoritesRepo.addToFavorites(apps = favorites.map { it.app })
            withContext(appCoroutineDispatcher.main) { navigator.pop() }
        }
    }
}

private fun SnapshotStateList<AppWithColor>.move(fromIndex: Int, toIndex: Int) {
    val app = removeAt(index = fromIndex)
    add(index = toIndex, element = app)
}
