package dev.mslalith.focuslauncher.feature.appdrawerpage.bottomsheet.updateappdisplayname

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
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.screens.UpdateAppDisplayNameBottomSheetScreen
import dev.mslalith.focuslauncher.feature.appdrawerpage.bottomsheet.updateappdisplayname.UpdateAppDisplayNameBottomSheetUiEvent.UpdateDisplayName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateAppDisplayNameBottomSheetPresenter @AssistedInject constructor(
    @Assisted private val screen: UpdateAppDisplayNameBottomSheetScreen,
    @Assisted private val navigator: Navigator,
    private val appDrawerRepo: AppDrawerRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<UpdateAppDisplayNameBottomSheetState> {

    @CircuitInject(UpdateAppDisplayNameBottomSheetScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: UpdateAppDisplayNameBottomSheetScreen,
            navigator: Navigator
        ): UpdateAppDisplayNameBottomSheetPresenter
    }

    @Composable
    override fun present(): UpdateAppDisplayNameBottomSheetState {
        val scope = rememberCoroutineScope()

        return UpdateAppDisplayNameBottomSheetState(
            app = screen.app
        ) {
            when (it) {
                is UpdateDisplayName -> scope.updateDisplayName(displayName = it.displayName)
            }
        }
    }

    private fun CoroutineScope.updateDisplayName(displayName: String) {
        launch(appCoroutineDispatcher.io) {
            appDrawerRepo.updateDisplayName(app = screen.app, displayName = displayName)
            withContext(appCoroutineDispatcher.main) { navigator.pop() }
        }
    }
}
