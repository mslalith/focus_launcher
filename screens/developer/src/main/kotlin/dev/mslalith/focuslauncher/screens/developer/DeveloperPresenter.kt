package dev.mslalith.focuslauncher.screens.developer

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.screens.DeveloperScreen

@CircuitInject(DeveloperScreen::class, SingletonComponent::class)
class DeveloperPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator
) : Presenter<DeveloperState> {

    @Composable
    override fun present(): DeveloperState {
        return DeveloperState {
            when (it) {
                DeveloperUiEvent.GoBack -> navigator.pop()
            }
        }
    }
}
