package dev.mslalith.focuslauncher.feature.clock24.widget

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.data.test.repository.FakeClockRepo
import dev.mslalith.focuslauncher.core.data.test.repository.settings.FakeClockSettingsRepo
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import dev.mslalith.focuslauncher.core.testing.extensions.instantOf
import dev.mslalith.focuslauncher.core.testing.extensions.withUtcTimeZone
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AppRobolectricTestRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class ClockWidgetUiComponentPresenterTest : PresenterTest<ClockWidgetUiComponentPresenter, ClockWidgetUiComponentState>() {

    private val clockRepo = FakeClockRepo()
    private val clockSettingsRepo = FakeClockSettingsRepo()

    override fun presenterUnderTest() = ClockWidgetUiComponentPresenter(
        clockRepo = clockRepo,
        clockSettingsRepo = clockSettingsRepo
    )

    @Test
    fun `01 - on toggle clock24 visibility, verify state change`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.showClock24).isTrue()

        clockSettingsRepo.toggleClock24()
        assertFor(expected = false) { it.showClock24 }

        clockSettingsRepo.toggleClock24()
        assertFor(expected = true) { it.showClock24 }
    }

    @Test
    fun `02 - on update clock alignment, verify state change`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.clockAlignment).isEqualTo(ClockAlignment.START)

        clockSettingsRepo.updateClockAlignment(clockAlignment = ClockAlignment.CENTER)
        assertFor(expected = ClockAlignment.CENTER) { it.clockAlignment }

        clockSettingsRepo.updateClockAlignment(clockAlignment = ClockAlignment.END)
        assertFor(expected = ClockAlignment.END) { it.clockAlignment }
    }

    @Test
    fun `03 - on update clock24 animation duration, verify state change`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.clock24AnimationDuration).isEqualTo(2100)

        clockSettingsRepo.updateClock24AnimationDuration(duration = 300)
        assertFor(expected = 300) { it.clock24AnimationDuration }

        clockSettingsRepo.updateClock24AnimationDuration(duration = 1800)
        assertFor(expected = 1800) { it.clock24AnimationDuration }
    }

    @Test
    fun `04 - on update time, verify state change`() = runPresenterTest {
        withUtcTimeZone {
            val state = awaitItem()

            clockRepo.setInstant(instantOf(hour = 23, minute = 4))
            state.eventSink(ClockWidgetUiComponentUiEvent.RefreshTime)
            assertFor(expected = "23:04") { it.currentTime }

            clockRepo.setInstant(instantOf(hour = 9, minute = 54))
            state.eventSink(ClockWidgetUiComponentUiEvent.RefreshTime)
            assertFor(expected = "09:54") { it.currentTime }
        }
    }

    @Test
    fun `05 - on update use 24 hour, verify state change`() = runPresenterTest {
        withUtcTimeZone {
            val state = awaitItem()
            clockRepo.setInstant(instantOf(hour = 23, minute = 4))
            state.eventSink(ClockWidgetUiComponentUiEvent.RefreshTime)
            assertFor(expected = "23:04") { it.currentTime }

            clockSettingsRepo.toggleUse24Hour()
            assertFor(expected = "11:04") { it.currentTime }

            clockSettingsRepo.toggleUse24Hour()
            assertFor(expected = "23:04") { it.currentTime }
        }
    }
}
