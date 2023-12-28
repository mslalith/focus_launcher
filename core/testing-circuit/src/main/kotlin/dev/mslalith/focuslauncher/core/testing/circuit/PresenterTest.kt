package dev.mslalith.focuslauncher.core.testing.circuit

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.turbine.ReceiveTurbine
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuit.overlay.Overlay
import com.slack.circuit.overlay.OverlayHost
import com.slack.circuit.overlay.OverlayHostData
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.presenterTestOf
import com.slack.circuit.test.test
import dev.mslalith.focuslauncher.core.testing.CoroutineTest

abstract class PresenterTest<P : Presenter<S>, S : CircuitUiState> : CoroutineTest() {

    protected lateinit var navigator: FakeNavigator

    abstract fun presenterUnderTest(): P

    protected fun runPresenterTest(
        block: suspend ReceiveTurbine<S>.() -> Unit
    ) = runCoroutineTest {
        navigator = FakeNavigator()
        val presenter = presenterUnderTest()
        presenter.test(block = block)
    }

    protected fun runPresenterTest(
        initialValue: S,
        block: suspend ReceiveTurbine<S>.() -> Unit
    ) = runCoroutineTest {
        navigator = FakeNavigator()
        val presenter = presenterUnderTest()
        presenterTestOf(
            presentFunction = {
                var result: S by remember { mutableStateOf(value = initialValue) }
                CompositionLocalProvider(LocalOverlayHost provides NoOpOverlayHost) {
                    result = presenter.present()
                }
                result
            },
            block = {
                awaitItem() // drop initial value
                block()
            }
        )
    }
}

private object NoOpOverlayHost : OverlayHost {
    override val currentOverlayData: OverlayHostData<Any>? = null

    override suspend fun <Result : Any> show(overlay: Overlay<Result>): Result {
        TODO()
    }
}
