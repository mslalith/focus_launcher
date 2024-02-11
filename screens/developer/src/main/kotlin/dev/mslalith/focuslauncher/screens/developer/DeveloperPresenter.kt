package dev.mslalith.focuslauncher.screens.developer

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.screens.DeveloperScreen
import dev.mslalith.focuslauncher.screens.developer.file.FavoritesCacheFileInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DeveloperPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val favoritesRepo: FavoritesRepo,
    private val favoritesCacheFileInteractor: FavoritesCacheFileInteractor,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<DeveloperState> {

    @CircuitInject(DeveloperScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): DeveloperPresenter
    }

    private var isFavoritesReading by mutableStateOf(value = false)
    private var isFavoritesSaving by mutableStateOf(value = false)

    @Composable
    override fun present(): DeveloperState {
        val scope = rememberCoroutineScope()

        fun runInIO(block: suspend () -> Unit) {
            scope.launch(appCoroutineDispatcher.io) { block() }
        }

        return DeveloperState(
            defaultFavoritesName = favoritesCacheFileInteractor.fileName,
            isFavoritesReading = isFavoritesReading,
            isFavoritesSaving = isFavoritesSaving,
        ) {
            when (it) {
                DeveloperUiEvent.GoBack -> navigator.pop()
                is DeveloperUiEvent.ReadFavoritesFromUri -> runInIO { readFavorites(uri = it.uri) }
                is DeveloperUiEvent.SaveFavoritesFromUri -> runInIO { saveFavorites(uri = it.uri) }
            }
        }
    }

    private suspend fun readFavorites(uri: Uri) {
        isFavoritesReading = true
        val favorites = favoritesCacheFileInteractor.readContents(uri = uri)
        if (favorites.isNotEmpty()) {
            favoritesRepo.clearFavorites()
            favoritesRepo.addToFavorites(apps = favorites)
        }
        isFavoritesReading = false
    }

    private suspend fun saveFavorites(uri: Uri) {
        isFavoritesSaving = true
        val favorites = favoritesRepo.onlyFavoritesFlow.first()
        favoritesCacheFileInteractor.writeContents(uri = uri, content = favorites)
        isFavoritesSaving = false
    }
}
