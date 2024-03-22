package dev.mslalith.focuslauncher.screens.about

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.screens.AboutScreen

class AboutPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator
) : Presenter<AboutState> {

    @CircuitInject(AboutScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): AboutPresenter
    }

    @Composable
    override fun present(): AboutState {
        return AboutState {
            when (it) {
                AboutUiEvent.GoBack -> navigator.pop()
            }
        }
    }
}
