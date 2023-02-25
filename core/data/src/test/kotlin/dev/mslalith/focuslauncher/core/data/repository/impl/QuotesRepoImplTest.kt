package dev.mslalith.focuslauncher.core.data.repository.impl

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.common.getOrNull
import dev.mslalith.focuslauncher.core.data.base.RepoTest
import dev.mslalith.focuslauncher.core.data.helpers.dummyQuoteFor
import dev.mslalith.focuslauncher.core.data.model.TestComponents
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.QUOTES_LIMIT_PER_PAGE
import dev.mslalith.focuslauncher.core.model.Quote
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
internal class QuotesRepoImplTest : RepoTest<QuotesRepoImpl>() {

    override fun provideRepo(testComponents: TestComponents): QuotesRepoImpl {
        return object : QuotesRepoImpl(
            quotesApi = testComponents.apis.quotesApi,
            quotesDao = testComponents.database.quotesDao(),
            appCoroutineDispatcher = testComponents.appCoroutineDispatcher,
            quoteResponseToRoomMapper = testComponents.mappers.quoteResponseToRoomMapper,
            quoteToRoomMapper = testComponents.mappers.quoteToRoomMapper
        ) {
            override fun getRandomIndex(size: Int) = 0
        }
    }

    @Test
    fun `fetch quotes and make sure they are added to database`() = runCoroutineTest {
        val job = launch {
            repo.isFetchingQuotesStateFlow.test {
                assertThat(awaitItem()).isTrue()
                assertThat(awaitItem()).isFalse()
            }
        }
        repo.fetchQuotes(maxPages = 1)
        repo.nextRandomQuote()

        val quote = repo.currentQuoteStateFlow.awaitItem().getOrNull()
        assertThat(quote).isEqualTo(dummyQuoteFor(index = 0))

        job.cancelAndJoin()
    }

    @Test
    fun `when quotes are empty, initial quotes must be added`() = runCoroutineTest {
        repo.nextRandomQuote()
        val expected = Quote(
            id = "vuGBuD1oaev3",
            quote = "Do not go where the path may lead, go instead where there is no path and leave a trail.",
            author = "Ralph Waldo Emerson"
        )

        val quote = repo.currentQuoteStateFlow.awaitItem().getOrNull()
        assertThat(quote).isEqualTo(expected)
    }

    @Test
    fun `when one page of quotes are added, quotes size must be same`() = runCoroutineTest {
        repo.fetchQuotes(maxPages = 1)
        repo.nextRandomQuote()
        assertThat(repo.quotesSize()).isEqualTo(QUOTES_LIMIT_PER_PAGE)
    }

    @Test
    fun `when quotes size is less than limit, hasQuotesReachedLimit must return false`() = runCoroutineTest {
        repo.fetchQuotes(maxPages = 1)
        assertThat(repo.hasQuotesReachedLimit()).isFalse()
    }

    @Test
    fun `when quotes size is greater than limit, hasQuotesReachedLimit must return true`() = runCoroutineTest {
        repo.fetchQuotes(maxPages = 2)
        assertThat(repo.hasQuotesReachedLimit()).isTrue()
    }
}
