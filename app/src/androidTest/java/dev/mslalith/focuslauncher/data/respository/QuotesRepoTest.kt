package dev.mslalith.focuslauncher.data.respository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.FakeQuotesRepo
import dev.mslalith.focuslauncher.data.models.Outcome
import dev.mslalith.focuslauncher.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuotesRepoTest {

    private lateinit var quotesRepo: FakeQuotesRepo

    @Before
    fun setUp() {
        quotesRepo = FakeQuotesRepo()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getCurrentQuoteStateFlow() = runTest {
        quotesRepo.fetchQuotes(maxPages = 2)
        val indexes = listOf(7, 23)
        val job = launch {
            quotesRepo.currentQuoteStateFlow.test {
                var quoteOutcome = awaitItem()
                assertThat(quoteOutcome).isInstanceOf(Outcome.None::class.java)
                indexes.forEach { index ->
                    quoteOutcome = awaitItem()
                    assertThat(quoteOutcome).isInstanceOf(Outcome.Success::class.java)
                    val quote = (quoteOutcome as Outcome.Success).value
                    assertThat(quote).isEqualTo(quotesRepo.getQuoteAt(index))
                }
                expectNoEvents()
            }
        }

        indexes.forEach { index ->
            quotesRepo.randomIndex = index
            advanceTimeBy(1_000)
            quotesRepo.nextRandomQuote()
        }
        job.join()
    }

    @Test
    fun isFetchingQuotesStateFlow() = runTest {
        val job = launch {
            quotesRepo.isFetchingQuotesStateFlow.test {
                assertThat(awaitItem()).isFalse()
                assertThat(awaitItem()).isTrue()
                expectNoEvents()
            }
        }

        quotesRepo.fetchQuotes()
        job.join()
    }

    @Test
    fun nextRandomQuote() = runTest {
        quotesRepo.fetchQuotes(maxPages = 2)
        val indexes = listOf(7, 23)
        indexes.forEach { index ->
            quotesRepo.randomIndex = index
            quotesRepo.nextRandomQuote()
            val quote = (quotesRepo.currentQuoteStateFlow.value as Outcome.Success).value
            assertThat(quote).isEqualTo(quotesRepo.getQuoteAt(index))
        }
    }

    @Test
    fun fetchQuotes() = runTest {
        val maxPages = 2
        val totalQuotes = maxPages * Constants.Defaults.QUOTES_LIMIT_PER_PAGE
        quotesRepo.fetchQuotes(maxPages)
        assertThat(quotesRepo.quotesSize()).isEqualTo(totalQuotes)
    }

    @Test
    fun hasQuotesReachedLimit() = runTest {
        val pagesToReachMax = Constants.Defaults.QUOTES_LIMIT / Constants.Defaults.QUOTES_LIMIT_PER_PAGE
        quotesRepo.fetchQuotes(pagesToReachMax - 1)
        assertThat(quotesRepo.hasQuotesReachedLimit()).isFalse()
        quotesRepo.fetchQuotes(pagesToReachMax + 1)
        assertThat(quotesRepo.hasQuotesReachedLimit()).isTrue()
    }

    @Test
    fun quotesSize() = runTest {
        val maxPages = 3
        val totalQuotes = maxPages * Constants.Defaults.QUOTES_LIMIT_PER_PAGE
        quotesRepo.fetchQuotes(maxPages)
        assertThat(quotesRepo.quotesSize()).isEqualTo(totalQuotes)
    }
}
