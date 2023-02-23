package dev.mslalith.focuslauncher.core.data.repository.impl

import dev.mslalith.focuslauncher.core.data.base.RepoTest
import dev.mslalith.focuslauncher.core.data.model.TestComponents
import dev.mslalith.focuslauncher.core.data.verifyChange
import dev.mslalith.focuslauncher.core.model.Theme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
internal class ThemeRepoImplTest : RepoTest<ThemeRepoImpl>() {

    override fun provideRepo(testComponents: TestComponents) = ThemeRepoImpl(themeDataStore = testComponents.dataStore)

    @Test
    fun `verify theme change`() = runCoroutineTest {
        verifyChange(
            flow = repo.currentThemeFlow,
            initialItem = null,
            mutate = {
                val newTheme = Theme.NOT_WHITE
                repo.changeTheme(newTheme)
                newTheme
            }
        )
    }
}
