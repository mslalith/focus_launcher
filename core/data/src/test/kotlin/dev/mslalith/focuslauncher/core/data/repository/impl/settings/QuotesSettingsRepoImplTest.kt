package dev.mslalith.focuslauncher.core.data.repository.impl.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.Quotes.DEFAULT_SHOW_QUOTES
import dev.mslalith.focuslauncher.core.data.verifyChange
import dev.mslalith.focuslauncher.core.data.base.DataStoreTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
internal class QuotesSettingsRepoImplTest : DataStoreTest<QuotesSettingsRepoImpl>() {

    override fun provideRepo(dataStore: DataStore<Preferences>) = QuotesSettingsRepoImpl(settingsDataStore = dataStore)

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
