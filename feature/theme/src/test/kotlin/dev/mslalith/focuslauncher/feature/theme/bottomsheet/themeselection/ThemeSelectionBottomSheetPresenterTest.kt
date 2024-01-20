package dev.mslalith.focuslauncher.feature.theme.bottomsheet.themeselection

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.domain.theme.ChangeThemeUseCase
import dev.mslalith.focuslauncher.core.domain.theme.GetThemeUseCase
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AppRobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class ThemeSelectionBottomSheetPresenterTest : PresenterTest<ThemeSelectionBottomSheetPresenter, ThemeSelectionBottomSheetState>() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getThemeUseCase: GetThemeUseCase

    @Inject
    lateinit var changeThemeUseCase: ChangeThemeUseCase

    @Inject
    lateinit var appCoroutineDispatcher: AppCoroutineDispatcher

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    override fun presenterUnderTest() = ThemeSelectionBottomSheetPresenter(
        navigator = navigator,
        getThemeUseCase = getThemeUseCase,
        changeThemeUseCase = changeThemeUseCase,
        appCoroutineDispatcher = appCoroutineDispatcher
    )

    @Test
    fun `01 - when theme is updated, new theme should be returned`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.currentTheme).isEqualTo(Theme.FOLLOW_SYSTEM)

        state.eventSink(ThemeSelectionBottomSheetUiEvent.SelectedTheme(theme = Theme.SAID_DARK))
        assertThat(awaitItem().currentTheme).isEqualTo(Theme.SAID_DARK)

        navigator.awaitPop()
        cancelAndIgnoreRemainingEvents()
    }
}
