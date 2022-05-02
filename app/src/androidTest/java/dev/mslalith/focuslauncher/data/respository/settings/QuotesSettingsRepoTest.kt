package dev.mslalith.focuslauncher.data.respository.settings

import dev.mslalith.focuslauncher.data.respository.DataStoreTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.utils.Constants
import kotlinx.coroutines.flow.first
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuotesSettingsRepoTest : DataStoreTest<QuotesSettingsRepo>(
    setupRepo = { QuotesSettingsRepo(it) }
) {

    @Test
    fun getShowQuotesFlow() = runTest {
        val value = repo.showQuotesFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.Quotes.DEFAULT_SHOW_QUOTES)
    }

    @Test
    fun toggleShowQuotes() = runTest {
        repo.toggleShowQuotes()
        val value = repo.showQuotesFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.Quotes.DEFAULT_SHOW_QUOTES)
    }
}
