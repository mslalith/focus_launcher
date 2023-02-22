package dev.mslalith.focuslauncher.core.data.repository.impl.settings

import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.Quotes.DEFAULT_SHOW_QUOTES
import dev.mslalith.focuslauncher.core.testing.DataStoreTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
internal class QuotesSettingsRepoImplTest : DataStoreTest<QuotesSettingsRepoImpl>(
    setupRepo = { QuotesSettingsRepoImpl(settingsDataStore = it) }
) {

    @Test
    fun `verify show quotes change`() = runCoroutineTest {
        verifyChange(
            flow = repo.showQuotesFlow,
            initialItem = DEFAULT_SHOW_QUOTES,
            mutate = {
                repo.toggleShowQuotes()
                true
            }
        )
    }
}
