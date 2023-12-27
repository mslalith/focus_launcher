package dev.mslalith.focuslauncher.feature.appdrawerpage.bottomsheet.moreoptions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.screens.AppMoreOptionsBottomSheetScreen
import dev.mslalith.focuslauncher.feature.appdrawerpage.bottomsheet.moreoptions.AppMoreOptionsBottomSheetUiEvent.AddToFavorites
import dev.mslalith.focuslauncher.feature.appdrawerpage.bottomsheet.moreoptions.AppMoreOptionsBottomSheetUiEvent.AddToHiddenApps
import dev.mslalith.focuslauncher.feature.appdrawerpage.bottomsheet.moreoptions.AppMoreOptionsBottomSheetUiEvent.GoBack
import dev.mslalith.focuslauncher.feature.appdrawerpage.bottomsheet.moreoptions.AppMoreOptionsBottomSheetUiEvent.RemoveFromFavorites
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppMoreOptionsBottomSheetPresenter @AssistedInject constructor(
    @Assisted private val screen: AppMoreOptionsBottomSheetScreen,
    @Assisted private val navigator: Navigator,
    private val favoritesRepo: FavoritesRepo,
    private val hiddenAppsRepo: HiddenAppsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<AppMoreOptionsBottomSheetState> {

    @CircuitInject(AppMoreOptionsBottomSheetScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: AppMoreOptionsBottomSheetScreen,
            navigator: Navigator
        ): AppMoreOptionsBottomSheetPresenter
    }

    @Composable
    override fun present(): AppMoreOptionsBottomSheetState {
        val scope = rememberCoroutineScope()

        return AppMoreOptionsBottomSheetState(
            appDrawerItem = screen.appDrawerItem
        ) {
            when (it) {
                GoBack -> navigator.pop()
                is AddToFavorites -> scope.addToFavoritesAndClose(app = it.app)
                is AddToHiddenApps -> scope.addToHiddenAppsAndClose(app = it.app, removeFromFavorites = it.removeFromFavorites)
                is RemoveFromFavorites -> scope.removeFromFavoritesAndClose(app = it.app)
            }
        }
    }

    private fun CoroutineScope.addToFavoritesAndClose(app: App) {
        launch(appCoroutineDispatcher.io) {
            favoritesRepo.addToFavorites(app = app)
            withContext(appCoroutineDispatcher.main) { navigator.pop() }
        }
    }

    private fun CoroutineScope.removeFromFavoritesAndClose(app: App) {
        launch(appCoroutineDispatcher.io) {
            favoritesRepo.removeFromFavorites(packageName = app.packageName)
            withContext(appCoroutineDispatcher.main) { navigator.pop() }
        }
    }

    private fun CoroutineScope.addToHiddenAppsAndClose(app: App, removeFromFavorites: Boolean) {
        launch(appCoroutineDispatcher.io) {
            if (removeFromFavorites) favoritesRepo.removeFromFavorites(packageName = app.packageName)
            hiddenAppsRepo.addToHiddenApps(app = app)
            withContext(appCoroutineDispatcher.main) { navigator.pop() }
        }
    }
}
