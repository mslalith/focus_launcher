package dev.mslalith.focuslauncher.feature.settingspage

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.test.TestAppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.test.repository.settings.FakeAppDrawerSettingsRepo
import dev.mslalith.focuslauncher.core.data.test.repository.settings.FakeGeneralSettingsRepo
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import dev.mslalith.focuslauncher.feature.settingspage.utils.isDefaultLauncher
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AppRobolectricTestRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class SettingsPagePresenterTest : PresenterTest<SettingsPagePresenter, SettingsPageState>() {

    private val generalSettingsRepo = FakeGeneralSettingsRepo()
    private val appDrawerSettingsRepo = FakeAppDrawerSettingsRepo()
    private val appCoroutineDispatcher = TestAppCoroutineDispatcher()

    @Before
    fun setup() {
        mockkStatic("dev.mslalith.focuslauncher.feature.settingspage.utils.ExtensionsKt")
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    override fun presenterUnderTest() = SettingsPagePresenter(
        navigator = navigator,
        generalSettingsRepo = generalSettingsRepo,
        appDrawerSettingsRepo = appDrawerSettingsRepo,
        appCoroutineDispatcher = appCoroutineDispatcher
    )

    @Test
    fun `01 - verify status bar toggle change`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.showStatusBar).isFalse()

        state.eventSink(SettingsPageUiEvent.ToggleStatusBarVisibility)
        assertForTrue { it.showStatusBar }
    }

    @Test
    fun `02 - verify notification shade toggle change`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.canDrawNotificationShade).isTrue()

        state.eventSink(SettingsPageUiEvent.ToggleNotificationShade)
        assertForFalse { it.canDrawNotificationShade }
    }

    @Test
    fun `03 - verify refresh default launcher change`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.isDefaultLauncher).isTrue()

        every { context.isDefaultLauncher() } returns false

        state.eventSink(SettingsPageUiEvent.RefreshIsDefaultLauncher(context = context))
        assertForFalse { it.isDefaultLauncher }
    }
}
