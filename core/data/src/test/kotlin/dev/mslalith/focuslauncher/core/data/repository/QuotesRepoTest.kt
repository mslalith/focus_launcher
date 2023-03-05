package dev.mslalith.focuslauncher.core.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.getOrNull
import dev.mslalith.focuslauncher.core.common.providers.randomnumber.test.TestRandomNumberProvider
import dev.mslalith.focuslauncher.core.data.database.AppDatabase
import dev.mslalith.focuslauncher.core.data.utils.Constants
import dev.mslalith.focuslauncher.core.data.utils.dummyQuoteFor
import dev.mslalith.focuslauncher.core.model.Quote
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    lateinit var testRandomNumberProvider: TestRandomNumberProvider

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
        backgroundScope.launch {
            repo.isFetchingQuotesStateFlow.test {
                assertThat(awaitItem()).isTrue()
                assertThat(awaitItem()).isFalse()
            }
        }
        repo.fetchQuotes(maxPages = 1)

        testRandomNumberProvider.setRandomNumber(randomNumber = 6)
        repo.nextRandomQuote()

        val quote = repo.currentQuoteStateFlow.awaitItem().getOrNull()
        assertThat(quote).isEqualTo(dummyQuoteFor(index = 6))
    }

    @Test
    fun `when quotes are empty, initial quotes must be added`() = runCoroutineTest {
        testRandomNumberProvider.setRandomNumber(randomNumber = 3)
        repo.nextRandomQuote()

        assertThat(repo.currentQuoteStateFlow.awaitItem().getOrNull()).isEqualTo(
            Quote(
                id = "2C-BAEVx44Os",
                quote = "I walk slowly, but I never walk backward.",
                author = "Abraham Lincoln"
            )
        )

        testRandomNumberProvider.setRandomNumber(randomNumber = 0)
        repo.nextRandomQuote()

        assertThat(repo.currentQuoteStateFlow.awaitItem().getOrNull()).isEqualTo(
            Quote(
                id = "vuGBuD1oaev3",
                quote = "Do not go where the path may lead, go instead where there is no path and leave a trail.",
                author = "Ralph Waldo Emerson"
            )
        )
    }

    @Test
    fun `when one page of quotes are added, quotes size must be same`() = runCoroutineTest {
        repo.fetchQuotes(maxPages = 1)
        assertThat(repo.quotesSize()).isEqualTo(Constants.Defaults.QUOTES_LIMIT_PER_PAGE)
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
