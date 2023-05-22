package dev.mslalith.focuslauncher.core.domain.theme

import com.google.common.truth.Truth.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.data.repository.ThemeRepo
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import kotlinx.coroutines.CoroutineScope
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
internal class ChangeThemeUseCaseTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var themeRepo: ThemeRepo

    private lateinit var changeThemeUseCase: ChangeThemeUseCase

    @Before
    fun setup() {
        hiltRule.inject()
        changeThemeUseCase = ChangeThemeUseCase(themeRepo = themeRepo)
    }

    @Test
    fun `01 - when theme set to Dark, it must return the same`() = runCoroutineTest {
        assertTheme(expected = Theme.FOLLOW_SYSTEM)
        changeThemeUseCase(theme = Theme.SAID_DARK)
        assertTheme(expected = Theme.SAID_DARK)
    }

    context (CoroutineScope)
    private suspend fun assertTheme(expected: Theme) {
        assertThat(themeRepo.currentThemeFlow.awaitItem()).isEqualTo(expected)
    }
}
