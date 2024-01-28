package dev.mslalith.focuslauncher.feature.lunarcalendar.bottomsheet.lunarphasewidgetsettings

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.circuitoverlay.OverlayResultScreen
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.test.TestAppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.test.repository.FakeClockRepo
import dev.mslalith.focuslauncher.core.data.test.repository.FakeLunarPhaseDetailsRepo
import dev.mslalith.focuslauncher.core.data.test.repository.settings.FakeLunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.screens.CurrentPlaceScreen
import dev.mslalith.focuslauncher.core.screens.LunarPhaseWidgetSettingsBottomSheetScreen.Result.PopAndGoto
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AppRobolectricTestRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class LunarPhaseWidgetSettingsBottomSheetPresenterTest : PresenterTest<LunarPhaseWidgetSettingsBottomSheetPresenter, LunarPhaseWidgetSettingsBottomSheetState>() {

    private val clockRepo = FakeClockRepo()
    private val lunarPhaseSettingsRepo = FakeLunarPhaseSettingsRepo()
    private val lunarPhaseDetailsRepo = FakeLunarPhaseDetailsRepo()
    private val appCoroutineDispatcher = TestAppCoroutineDispatcher()

    override fun presenterUnderTest() = LunarPhaseWidgetSettingsBottomSheetPresenter(
        navigator = navigator,
        clockRepo = clockRepo,
        lunarPhaseSettingsRepo = lunarPhaseSettingsRepo,
        lunarPhaseDetailsRepo = lunarPhaseDetailsRepo,
        appCoroutineDispatcher = appCoroutineDispatcher
    )

    @Test
    fun `01 - when show lunar phase toggled, verify state change`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.showLunarPhase).isTrue()

        state.eventSink(LunarPhaseWidgetSettingsBottomSheetUiEvent.ToggleShowLunarPhase)
        assertForFalse { it.showLunarPhase }
    }

    @Test
    fun `02 - when show illumination percent toggled, verify state change`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.showIlluminationPercent).isTrue()

        state.eventSink(LunarPhaseWidgetSettingsBottomSheetUiEvent.ToggleShowIlluminationPercent)
        assertForFalse { it.showIlluminationPercent }
    }

    @Test
    fun `03 - when show upcoming phase details toggled, verify state change`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.showUpcomingPhaseDetails).isTrue()

        state.eventSink(LunarPhaseWidgetSettingsBottomSheetUiEvent.ToggleShowUpcomingPhaseDetails)
        assertForFalse { it.showUpcomingPhaseDetails }
    }

    @Test
    fun `04 - verify goTo screen`() = runPresenterTest {
        val state = awaitItem()
        state.eventSink(LunarPhaseWidgetSettingsBottomSheetUiEvent.Goto(CurrentPlaceScreen))

        @Suppress("UNCHECKED_CAST")
        val nextScreen = navigator.awaitNextScreen() as OverlayResultScreen<PopAndGoto>
        assertThat(nextScreen.result?.screen).isEqualTo(CurrentPlaceScreen)
    }
}
