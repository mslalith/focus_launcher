package dev.mslalith.focuslauncher.core.data.repository.settings

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.data.helpers.verifyChange
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_DURATION
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_ALIGNMENT
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.Clock.DEFAULT_SHOW_CLOCK_24
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
internal class ClockSettingsRepoTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: ClockSettingsRepo

    @Before
    fun setup() {
        hiltRule.inject()
    }

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
