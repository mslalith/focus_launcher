package dev.mslalith.focuslauncher.feature.theme

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
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
class LauncherThemePresenterTest : PresenterTest<LauncherThemePresenter, LauncherThemeState>() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getThemeUseCase: GetThemeUseCase

    @Inject
    lateinit var changeThemeUseCase: ChangeThemeUseCase

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    override fun presenterUnderTest() = LauncherThemePresenter(
        getThemeUseCase = getThemeUseCase
    )

    @Test
    fun `01 - initially default theme should be returned`() = runPresenterTest {
        assertThat(awaitItem().theme).isEqualTo(Theme.FOLLOW_SYSTEM)
    }

    @Test
    fun `02 - when theme is updated, new theme should be returned`() = runPresenterTest {
        assertThat(awaitItem().theme).isEqualTo(Theme.FOLLOW_SYSTEM)
        changeThemeUseCase(Theme.SAID_DARK)
        assertThat(awaitItem().theme).isEqualTo(Theme.SAID_DARK)
    }
}
