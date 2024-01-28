package dev.mslalith.focuslauncher.feature.clock24.bottomsheet.clockwidgetsettings

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.test.TestAppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.test.repository.FakeClockRepo
import dev.mslalith.focuslauncher.core.data.test.repository.settings.FakeClockSettingsRepo
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AppRobolectricTestRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class ClockWidgetSettingsBottomSheetPresenterTest : PresenterTest<ClockWidgetSettingsBottomSheetPresenter, ClockWidgetSettingsBottomSheetState>() {

    private val clockRepo = FakeClockRepo()
    private val clockSettingsRepo = FakeClockSettingsRepo()
    private val appCoroutineDispatcher = TestAppCoroutineDispatcher()

    override fun presenterUnderTest() = ClockWidgetSettingsBottomSheetPresenter(
        clockRepo = clockRepo,
        clockSettingsRepo = clockSettingsRepo,
        appCoroutineDispatcher = appCoroutineDispatcher
    )

    @Test
    fun `01 - when clock24 is toggled, verify state change`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.showClock24).isTrue()

        state.eventSink(ClockWidgetSettingsBottomSheetUiEvent.ToggleClock24)
        assertForFalse { it.showClock24 }
    }

    @Test
    fun `02 - when use 24 hour is toggled, verify state change`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.use24Hour).isTrue()

        state.eventSink(ClockWidgetSettingsBottomSheetUiEvent.ToggleUse24Hour)
        assertForFalse { it.use24Hour }
    }

    @Test
    fun `03 - when clock alignment is updated, verify state change`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.clockAlignment).isEqualTo(ClockAlignment.START)

        val expected = ClockAlignment.CENTER
        state.eventSink(ClockWidgetSettingsBottomSheetUiEvent.UpdateClockAlignment(clockAlignment = expected))
        assertFor(expected = expected) { it.clockAlignment }
    }

    @Test
    fun `04 - when clock24 animation duration is toggled, verify state change`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.clock24AnimationDuration).isEqualTo(2100)

        val expected = 2400
        state.eventSink(ClockWidgetSettingsBottomSheetUiEvent.UpdateClock24AnimationDuration(duration = expected))
        assertFor(expected = expected) { it.clock24AnimationDuration }
    }
}
