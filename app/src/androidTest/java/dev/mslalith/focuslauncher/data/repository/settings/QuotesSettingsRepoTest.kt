package dev.mslalith.focuslauncher.data.repository.settings

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.data.network.repository.settings.QuotesSettingsRepo
import dev.mslalith.focuslauncher.data.repository.DataStoreTest
import dev.mslalith.focuslauncher.data.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
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
