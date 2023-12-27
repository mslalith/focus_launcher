package dev.mslalith.focuslauncher.core.testing.circuit

import app.cash.turbine.ReceiveTurbine
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.test.FakeNavigator
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
}
