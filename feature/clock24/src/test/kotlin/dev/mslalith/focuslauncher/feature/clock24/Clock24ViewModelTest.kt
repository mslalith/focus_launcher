package dev.mslalith.focuslauncher.feature.clock24

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.ClockRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.ClockSettingsRepo
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.extensions.assertFor
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
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
internal class Clock24ViewModelTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

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
    fun `on toggle clock24 visibility, verify state change`() = runCoroutineTest {
        assertThat(viewModel.clock24State.awaitItem().showClock24).isTrue()

        viewModel.toggleClock24()
        viewModel.clock24State.assertFor(expected = false) { it.showClock24 }

        viewModel.toggleClock24()
        viewModel.clock24State.assertFor(expected = true) { it.showClock24 }
    }

    @Test
    fun `on update clock alignment, verify state change`() = runCoroutineTest {
        assertThat(viewModel.clock24State.awaitItem().clockAlignment).isEqualTo(ClockAlignment.START)

        viewModel.updateClockAlignment(clockAlignment = ClockAlignment.CENTER)
        viewModel.clock24State.assertFor(expected = ClockAlignment.CENTER) { it.clockAlignment }

        viewModel.updateClockAlignment(clockAlignment = ClockAlignment.END)
        viewModel.clock24State.assertFor(expected = ClockAlignment.END) { it.clockAlignment }
    }

    @Test
    fun `on update clock24 animation duration, verify state change`() = runCoroutineTest {
        assertThat(viewModel.clock24State.awaitItem().clock24AnimationDuration).isEqualTo(2100)

        viewModel.updateClock24AnimationDuration(duration = 300)
        viewModel.clock24State.assertFor(expected = 300) { it.clock24AnimationDuration }

        viewModel.updateClock24AnimationDuration(duration = 1800)
        viewModel.clock24State.assertFor(expected = 1800) { it.clock24AnimationDuration }
    }
}
