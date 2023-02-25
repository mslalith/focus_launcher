package dev.mslalith.focuslauncher.core.data.repository.impl.settings

import dev.mslalith.focuslauncher.core.data.base.RepoTest
import dev.mslalith.focuslauncher.core.data.helpers.verifyChange
import dev.mslalith.focuslauncher.core.data.model.TestComponents
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.Quotes.DEFAULT_SHOW_QUOTES
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
internal class QuotesSettingsRepoImplTest : RepoTest<QuotesSettingsRepoImpl>() {

    override fun provideRepo(testComponents: TestComponents) = QuotesSettingsRepoImpl(settingsDataStore = testComponents.dataStore)

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
