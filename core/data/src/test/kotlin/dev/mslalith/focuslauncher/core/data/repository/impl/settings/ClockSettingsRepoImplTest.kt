package dev.mslalith.focuslauncher.core.data.repository.impl.settings

import dev.mslalith.focuslauncher.core.data.base.RepoTest
import dev.mslalith.focuslauncher.core.data.model.TestComponents
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_DURATION
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_ALIGNMENT
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.Clock.DEFAULT_SHOW_CLOCK_24
import dev.mslalith.focuslauncher.core.data.verifyChange
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
internal class ClockSettingsRepoImplTest : RepoTest<ClockSettingsRepoImpl>() {

    override fun provideRepo(testComponents: TestComponents) = ClockSettingsRepoImpl(settingsDataStore = testComponents.dataStore)

    @Test
    fun `verify show clock24 change`() = runCoroutineTest {
        verifyChange(
            flow = repo.showClock24Flow,
            initialItem = DEFAULT_SHOW_CLOCK_24,
            mutate = {
                repo.toggleClock24()
                false
            }
        )
    }

    @Test
    fun `verify clock alignment change`() = runCoroutineTest {
        verifyChange(
            flow = repo.clockAlignmentFlow,
            initialItem = DEFAULT_CLOCK_ALIGNMENT,
            mutate = {
                val newItem = ClockAlignment.END
                repo.updateClockAlignment(newItem)
                newItem
            }
        )
    }

    @Test
    fun `verify clock24 animation duration change`() = runCoroutineTest {
        verifyChange(
            flow = repo.clock24AnimationDurationFlow,
            initialItem = DEFAULT_CLOCK_24_ANIMATION_DURATION,
            mutate = {
                val newItem = 1200
                repo.updateClock24AnimationDuration(newItem)
                newItem
            }
        )
    }
}
