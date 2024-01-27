package dev.mslalith.focuslauncher.screens.about

import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AppRobolectricTestRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class AboutPresenterTest : PresenterTest<AboutPresenter, AboutState>() {

    override fun presenterUnderTest() = AboutPresenter(
        navigator = navigator
    )

    @Test
    fun `01 - verify pop on back press`() = runPresenterTest {
        val state = awaitItem()
        state.eventSink(AboutUiEvent.GoBack)
        navigator.awaitPop()
    }
}
