package dev.mslalith.focuslauncher.core.domain.theme

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.data.database.usecase.datastore.ClearThemDataStoreUseCase
import dev.mslalith.focuslauncher.core.data.repository.ThemeRepo
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
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
internal class GetThemeUseCaseTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var themeRepo: ThemeRepo

    @Inject
    lateinit var clearThemeDataStoreUseCase: ClearThemDataStoreUseCase

    private lateinit var useCase: GetThemeUseCase

    @Before
    fun setup() {
        hiltRule.inject()
        useCase = GetThemeUseCase(themeRepo = themeRepo)
        runBlocking { clearThemeDataStoreUseCase() }
    }

    @Test
    fun `01 - initial theme must be default theme`() = runCoroutineTest {
        assertTheme(expected = Theme.FOLLOW_SYSTEM)
    }

    @Test
    fun `02 - when theme set to Light, it must return the same`() = runCoroutineTest {
        assertTheme(expected = Theme.FOLLOW_SYSTEM)
        themeRepo.changeTheme(theme = Theme.NOT_WHITE)
        assertTheme(expected = Theme.NOT_WHITE)
    }

    context (CoroutineScope)
    private suspend fun assertTheme(expected: Theme) {
        assertThat(useCase().awaitItem()).isEqualTo(expected)
    }
}
