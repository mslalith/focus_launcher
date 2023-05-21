package dev.mslalith.focuslauncher.feature.clock24

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.providers.clock.test.TestClockProvider
import dev.mslalith.focuslauncher.core.data.repository.ClockRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.ClockSettingsRepo
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.extensions.assertFor
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import dev.mslalith.focuslauncher.core.testing.extensions.instantOf
import dev.mslalith.focuslauncher.core.testing.extensions.withUtcTimeZone
import javax.inject.Inject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
internal class Clock24ViewModelTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var clockProvider: TestClockProvider

    @Inject
    lateinit var clockRepo: ClockRepo

    @Inject
    lateinit var clockSettingsRepo: ClockSettingsRepo

    @Inject
    lateinit var appCoroutineDispatcher: AppCoroutineDispatcher

    private lateinit var viewModel: Clock24ViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = Clock24ViewModel(
            clockRepo = clockRepo,
            clockSettingsRepo = clockSettingsRepo,
            appCoroutineDispatcher = appCoroutineDispatcher
        )
    }

    @Test
    fun `01 - on toggle clock24 visibility, verify state change`() = runCoroutineTest {
        assertThat(viewModel.clock24State.awaitItem().showClock24).isTrue()

        viewModel.toggleClock24()
        viewModel.clock24State.assertFor(expected = false) { it.showClock24 }

        viewModel.toggleClock24()
        viewModel.clock24State.assertFor(expected = true) { it.showClock24 }
    }

    @Test
    fun `02 - on update clock alignment, verify state change`() = runCoroutineTest {
        assertThat(viewModel.clock24State.awaitItem().clockAlignment).isEqualTo(ClockAlignment.START)

        viewModel.updateClockAlignment(clockAlignment = ClockAlignment.CENTER)
        viewModel.clock24State.assertFor(expected = ClockAlignment.CENTER) { it.clockAlignment }

        viewModel.updateClockAlignment(clockAlignment = ClockAlignment.END)
        viewModel.clock24State.assertFor(expected = ClockAlignment.END) { it.clockAlignment }
    }

    @Test
    fun `03 - on update clock24 animation duration, verify state change`() = runCoroutineTest {
        assertThat(viewModel.clock24State.awaitItem().clock24AnimationDuration).isEqualTo(2100)

        viewModel.updateClock24AnimationDuration(duration = 300)
        viewModel.clock24State.assertFor(expected = 300) { it.clock24AnimationDuration }

        viewModel.updateClock24AnimationDuration(duration = 1800)
        viewModel.clock24State.assertFor(expected = 1800) { it.clock24AnimationDuration }
    }

    @Test
    fun `04 - on update time, verify state change`() = runCoroutineTest {
        withUtcTimeZone {
            clockProvider.setInstant(instantOf(hour = 23, minute = 4))
            viewModel.refreshTime()
            viewModel.clock24State.assertFor(expected = "23:04") { it.currentTime }

            clockProvider.setInstant(instantOf(hour = 9, minute = 54))
            viewModel.refreshTime()
            viewModel.clock24State.assertFor(expected = "09:54") { it.currentTime }
        }
    }
}
