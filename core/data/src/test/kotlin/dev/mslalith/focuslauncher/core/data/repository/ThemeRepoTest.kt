package dev.mslalith.focuslauncher.core.data.repository

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.data.helpers.verifyChange
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_THEME
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
internal class ThemeRepoTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: ThemeRepo

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun `verify theme change`() = runCoroutineTest {
        verifyChange(
            flow = repo.currentThemeFlow,
            initialItem = DEFAULT_THEME,
            mutate = {
                val newTheme = Theme.NOT_WHITE
                repo.changeTheme(newTheme)
                newTheme
            }
        )
    }
}
