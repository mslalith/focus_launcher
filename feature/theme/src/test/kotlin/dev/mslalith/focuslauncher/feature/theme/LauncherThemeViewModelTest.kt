package dev.mslalith.focuslauncher.feature.theme

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.database.usecase.datastore.ClearThemeDataStoreUseCase
import dev.mslalith.focuslauncher.core.domain.theme.ChangeThemeUseCase
import dev.mslalith.focuslauncher.core.domain.theme.GetThemeUseCase
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.extensions.assertFor
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
internal class LauncherThemeViewModelTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getThemeUseCase: GetThemeUseCase

    @Inject
    lateinit var changeThemeUseCase: ChangeThemeUseCase

    @Inject
    lateinit var appCoroutineDispatcher: AppCoroutineDispatcher

    @Inject
    lateinit var clearThemeDataStoreUseCase: ClearThemeDataStoreUseCase

    private lateinit var viewModel: LauncherThemeViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = LauncherThemeViewModel(
            getThemeUseCase = getThemeUseCase,
            changeThemeUseCase = changeThemeUseCase,
            appCoroutineDispatcher = appCoroutineDispatcher
        )
        runBlocking { clearThemeDataStoreUseCase() }
    }

    @Test
    fun `01 - initial theme must be default theme`() = runCoroutineTest {
        viewModel.assertTheme(expected = Theme.FOLLOW_SYSTEM)
    }

    @Test
    fun `02 - when theme set to Light, it must return the same`() = runCoroutineTest {
        viewModel.assertTheme(expected = Theme.FOLLOW_SYSTEM)
        viewModel.changeTheme(theme = Theme.NOT_WHITE)
        viewModel.assertTheme(expected = Theme.NOT_WHITE)
    }
}

context (CoroutineScope)
private suspend fun LauncherThemeViewModel.assertTheme(expected: Theme) {
    currentTheme.assertFor(expected = expected) { it }
}
