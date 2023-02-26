package dev.mslalith.focuslauncher.core.data.repository.impl

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.getOrNull
import dev.mslalith.focuslauncher.core.data.database.AppDatabase
import dev.mslalith.focuslauncher.core.data.helpers.dummyQuoteFor
import dev.mslalith.focuslauncher.core.data.repository.QuotesRepo
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.QUOTES_LIMIT_PER_PAGE
import dev.mslalith.focuslauncher.core.model.Quote
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
internal class QuotesRepoTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: QuotesRepo

    @Inject
    lateinit var appDatabase: AppDatabase

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun teardown() {
        appDatabase.close()
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
