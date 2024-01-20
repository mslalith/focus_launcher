package dev.mslalith.focuslauncher.feature.quoteforyou.widget

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.test.TestAppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.model.State
import dev.mslalith.focuslauncher.core.common.model.getOrNull
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
class QuoteForYouUiComponentPresenterTest : PresenterTest<QuoteForYouUiComponentPresenter, QuoteForYouUiComponentState>() {

    private val quotesRepo = FakeQuotesRepo()
    private val quotesSettingsRepo = FakeQuotesSettingsRepo()
    private val appCoroutineDispatcher = TestAppCoroutineDispatcher()

    override fun presenterUnderTest() = QuoteForYouUiComponentPresenter(
        quotesRepo = quotesRepo,
        quotesSettingsRepo = quotesSettingsRepo,
        appCoroutineDispatcher = appCoroutineDispatcher
    )

    @Test
    fun `01 - when next quote is queried, verify it's state`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.currentQuote).isEqualTo(State.Initial)

        quotesRepo.nextQuoteIndex = 3
        state.eventSink(QuoteForYouUiComponentUiEvent.FetchNextQuote)
        assertFor(expected = dummyQuoteFor(index = 4)) { it.currentQuote.getOrNull() }
    }
}
