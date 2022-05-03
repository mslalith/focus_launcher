package dev.mslalith.focuslauncher.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.androidtest.shared.FakeQuotesApi
import dev.mslalith.focuslauncher.data.database.AppDatabase
import dev.mslalith.focuslauncher.data.dto.QuoteResponseToRoomMapper
import dev.mslalith.focuslauncher.data.dto.QuoteToRoomMapper
import dev.mslalith.focuslauncher.data.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuotesRepoTest {

    private lateinit var database: AppDatabase
    private lateinit var quotesRepo: QuotesRepo

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        quotesRepo = QuotesRepo(
            quotesApi = FakeQuotesApi(),
            quotesDao = database.quotesDao(),
            quoteToRoomMapper = QuoteToRoomMapper(database.quotesDao()),
            quoteResponseToRoomMapper = QuoteResponseToRoomMapper()
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getCurrentQuoteStateFlow() = runTest {
        quotesRepo.fetchQuotes(maxPages = 2)
        val indexes = listOf(7, 23)
        indexes.forEach { index ->
            val quote = quotesRepo.nextRandomQuoteTest(index)
            val quoteFromFlow = quotesRepo.currentQuoteStateFlow.value
            assertThat(quote).isEqualTo(quoteFromFlow)
        }
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

        advanceTimeBy(1_000)
        quotesRepo.fetchQuotes(maxPages = 2)
        job.join()
    }

    @Test
    fun nextRandomQuote() = runTest {
        quotesRepo.fetchQuotes(maxPages = 2)
        val indexes = listOf(7, 23)
        indexes.forEach { index ->
            val quote = quotesRepo.nextRandomQuoteTest(index)
            val quoteFromFlow = quotesRepo.currentQuoteStateFlow.value
            assertThat(quote).isEqualTo(quoteFromFlow)
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
