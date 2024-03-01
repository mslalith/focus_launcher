package dev.mslalith.focuslauncher.feature.quoteforyou.bottomsheet.quotewidgetsettings

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.test.TestAppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.model.State
import dev.mslalith.focuslauncher.core.common.model.getOrNull
import dev.mslalith.focuslauncher.core.common.network.test.FakeNetworkMonitor
import dev.mslalith.focuslauncher.core.data.test.repository.FakeQuotesRepo
import dev.mslalith.focuslauncher.core.data.test.repository.settings.FakeQuotesSettingsRepo
import dev.mslalith.focuslauncher.core.data.utils.dummyQuoteFor
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AppRobolectricTestRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class QuoteWidgetSettingsBottomSheetPresenterTest : PresenterTest<QuoteWidgetSettingsBottomSheetPresenter, QuoteWidgetSettingsBottomSheetState>() {

    private val quotesRepo = FakeQuotesRepo()
    private val quotesSettingsRepo = FakeQuotesSettingsRepo()
    private val networkMonitor = FakeNetworkMonitor()
    private val appCoroutineDispatcher = TestAppCoroutineDispatcher()

    override fun presenterUnderTest() = QuoteWidgetSettingsBottomSheetPresenter(
        quotesRepo = quotesRepo,
        quotesSettingsRepo = quotesSettingsRepo,
        networkMonitor = networkMonitor,
        appCoroutineDispatcher = appCoroutineDispatcher
    )

    @Test
    fun `01 - when quotes are toggled, state should be updated`() = runPresenterTest {
        val state = awaitItem()
        assertForFalse { it.showQuotes }

        state.eventSink(QuoteWidgetSettingsBottomSheetUiEvent.ToggleShowQuoteWidget)
        assertForTrue { it.showQuotes }
    }

    @Test
    fun `02 - when quotes are fetched, initial quote must be loaded`() = runPresenterTest {
        networkMonitor.goOnline()

        val state = awaitItem()
        state.eventSink(QuoteWidgetSettingsBottomSheetUiEvent.FetchQuoteWidget)

        assertThat(state.currentQuote).isEqualTo(State.Initial)
        assertThat(awaitItem().currentQuote.getOrNull()).isEqualTo(dummyQuoteFor(index = 1))

        cancelAndIgnoreRemainingEvents()
    }

    @Test
    fun `03 - fetch next quote`() = runPresenterTest {
        networkMonitor.goOnline()

        val state = awaitItem()
        assertThat(state.currentQuote).isEqualTo(State.Initial)
        assertThat(awaitItem().currentQuote.getOrNull()).isEqualTo(dummyQuoteFor(index = 1))

        quotesRepo.nextQuoteIndex = 3
        state.eventSink(QuoteWidgetSettingsBottomSheetUiEvent.FetchNextQuote)
        assertThat(awaitItem().currentQuote.getOrNull()).isEqualTo(dummyQuoteFor(index = 4))
    }
}
