package dev.mslalith.focuslauncher.screens.about

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.screens.AboutScreen

@CircuitInject(AboutScreen::class, SingletonComponent::class)
class AboutPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator
) : Presenter<AboutState> {

    @Composable
    override fun present(): AboutState {
        return AboutState {
            when (it) {
                AboutUiEvent.GoBack -> navigator.pop()
            }
        }
    }
}
