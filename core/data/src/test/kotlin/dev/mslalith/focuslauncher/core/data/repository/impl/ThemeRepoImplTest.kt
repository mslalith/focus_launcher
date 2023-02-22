package dev.mslalith.focuslauncher.core.data.repository.impl

import dev.mslalith.focuslauncher.core.data.verifyChange
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.core.testing.DataStoreTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
internal class ThemeRepoImplTest : DataStoreTest<ThemeRepoImpl>(
    setupRepo = { ThemeRepoImpl(themeDataStore = it) }
) {

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
