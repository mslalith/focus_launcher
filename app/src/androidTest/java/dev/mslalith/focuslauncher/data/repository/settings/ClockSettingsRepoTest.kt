package dev.mslalith.focuslauncher.data.repository.settings

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.data.model.ClockAlignment
import dev.mslalith.focuslauncher.data.network.repository.settings.ClockSettingsRepo
import dev.mslalith.focuslauncher.data.repository.DataStoreTest
import dev.mslalith.focuslauncher.data.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ClockSettingsRepoTest : DataStoreTest<ClockSettingsRepo>(
    setupRepo = { ClockSettingsRepo(it) }
) {

    @Test
    fun getShowClock24Flow() = runTest {
        val value = repo.showClock24Flow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.Clock.DEFAULT_SHOW_CLOCK_24)
    }

    @Test
    fun getClockAlignmentFlow() = runTest {
        val value = repo.clockAlignmentFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_ALIGNMENT)
    }

    @Test
    fun getClock24AnimationDurationFlow() = runTest {
        val value = repo.clock24AnimationDurationFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_DURATION)
    }

    @Test
    fun toggleClock24() = runTest {
        repo.toggleClock24()
        val value = repo.showClock24Flow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.Clock.DEFAULT_SHOW_CLOCK_24)
    }

    @Test
    fun updateClockAlignment() = runTest {
        ClockAlignment.values().forEach { clockAlignment ->
            repo.updateClockAlignment(clockAlignment)
            val value = repo.clockAlignmentFlow.first()
            assertThat(value).isEqualTo(clockAlignment)
        }
    }

    @Test
    fun updateClock24AnimationDuration() = runTest {
        val durations = List(8) { it * 1_000 }
        durations.forEach { duration ->
            repo.updateClock24AnimationDuration(duration)
            val value = repo.clock24AnimationDurationFlow.first()
            assertThat(value).isEqualTo(duration)
        }
    }
}
