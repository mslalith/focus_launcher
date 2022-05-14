package dev.mslalith.focuslauncher.data.repository.settings

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.androidtest.shared.DataStoreTest
import dev.mslalith.focuslauncher.data.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
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
