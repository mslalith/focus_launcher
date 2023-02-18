package dev.mslalith.focuslauncher.data.repository.settings

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.androidtest.shared.DataStoreTest
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import dev.mslalith.focuslauncher.data.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class ClockSettingsRepoTest : DataStoreTest<ClockSettingsRepo>(
    setupRepo = { ClockSettingsRepo(it) }
) {

    @Test
    fun getShowClock24Flow() = runCoroutineTest {
        val value = repo.showClock24Flow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.Clock.DEFAULT_SHOW_CLOCK_24)
    }

    @Test
    fun getClockAlignmentFlow() = runCoroutineTest {
        val value = repo.clockAlignmentFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_ALIGNMENT)
    }

    @Test
    fun getClock24AnimationDurationFlow() = runCoroutineTest {
        val value = repo.clock24AnimationDurationFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_DURATION)
    }

    @Test
    fun toggleClock24() = runCoroutineTest {
        repo.toggleClock24()
        val value = repo.showClock24Flow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.Clock.DEFAULT_SHOW_CLOCK_24)
    }

    @Test
    fun updateClockAlignment() = runCoroutineTest {
        ClockAlignment.values().forEach { clockAlignment ->
            repo.updateClockAlignment(clockAlignment)
            val value = repo.clockAlignmentFlow.first()
            assertThat(value).isEqualTo(clockAlignment)
        }
    }

    @Test
    fun updateClock24AnimationDuration() = runCoroutineTest {
        val durations = List(8) { it * 1_000 }
        durations.forEach { duration ->
            repo.updateClock24AnimationDuration(duration)
            val value = repo.clock24AnimationDurationFlow.first()
            assertThat(value).isEqualTo(duration)
        }
    }
}
