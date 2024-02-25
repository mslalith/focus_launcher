package dev.mslalith.focuslauncher.core.testing.circuit

import android.content.Context
import android.os.Parcel
import app.cash.turbine.ReceiveTurbine
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import io.mockk.every
import io.mockk.mockk

abstract class PresenterTest<P : Presenter<S>, S : CircuitUiState> : CoroutineTest() {

    protected lateinit var navigator: FakeNavigator
    protected lateinit var context: Context

    abstract fun presenterUnderTest(): P

    protected fun runPresenterTest(
        block: suspend ReceiveTurbine<S>.() -> Unit
    ) = runCoroutineTest {
        navigator = FakeNavigator(
            root = object : Screen {
                override fun describeContents(): Int = 0
                override fun writeToParcel(dest: Parcel, flags: Int) = Unit
            }
        )

        context = mockk()
        mockStrings()

        val presenter = presenterUnderTest()
        presenter.test(block = block)
    }

    context (ReceiveTurbine<S>)
    protected suspend fun <E> assertFor(
        expected: E,
        block: suspend (S) -> E
    ): S {
        var state = awaitItem()
        while (block(state) != expected) {
            state = awaitItem()
        }
        return state
    }

    context (ReceiveTurbine<S>)
    protected suspend fun <E> assertForTrue(
        block: suspend (S) -> E
    ): S = assertFor(expected = true, block = block)

    context (ReceiveTurbine<S>)
    protected suspend fun <E> assertForFalse(
        block: suspend (S) -> E
    ): S = assertFor(expected = false, block = block)

    private fun mockStrings() {
        every { context.getString(R.string.not_available) } returns "Not Available"
    }
}
