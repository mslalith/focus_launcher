package dev.mslalith.focuslauncher.feature.lunarcalendar

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.ClockRepo
import dev.mslalith.focuslauncher.core.data.repository.LunarPhaseDetailsRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.LunarPhaseSettingsRepo
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
class LunarCalendarViewModelTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var clockRepo: ClockRepo

    @Inject
    lateinit var lunarPhaseDetailsRepo: LunarPhaseDetailsRepo

    @Inject
    lateinit var lunarPhSettingsRepo: LunarPhaseSettingsRepo

    @Inject
    lateinit var appCoroutineDispatcher: AppCoroutineDispatcher

    private lateinit var viewModel: LunarCalendarViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = LunarCalendarViewModel(
            clockRepo = clockRepo,
            lunarPhaseDetailsRepo = lunarPhaseDetailsRepo,
            lunarPhaseSettingsRepo = lunarPhSettingsRepo,
            appCoroutineDispatcher = appCoroutineDispatcher
        )
    }

    @Test
    fun `01 - on toggle lunar phase visibility, verify state change`() = runCoroutineTest {
        assertThat(viewModel.lunarCalendarState.awaitItem().showLunarPhase).isTrue()

        viewModel.toggleShowLunarPhase()
        viewModel.lunarCalendarState.assertFor(expected = false) { it.showLunarPhase }

        viewModel.toggleShowLunarPhase()
        viewModel.lunarCalendarState.assertFor(expected = true) { it.showLunarPhase }
    }

    @Test
    fun `02 - on toggle lunar illumination visibility, verify state change`() = runCoroutineTest {
        assertThat(viewModel.lunarCalendarState.awaitItem().showIlluminationPercent).isTrue()

        viewModel.toggleShowIlluminationPercent()
        viewModel.lunarCalendarState.assertFor(expected = false) { it.showIlluminationPercent }

        viewModel.toggleShowIlluminationPercent()
        viewModel.lunarCalendarState.assertFor(expected = true) { it.showIlluminationPercent }
    }

    @Test
    fun `03 - on toggle upcoming lunar visibility, verify state change`() = runCoroutineTest {
        assertThat(viewModel.lunarCalendarState.value.showUpcomingPhaseDetails).isTrue()

        viewModel.toggleShowUpcomingPhaseDetails()
        viewModel.lunarCalendarState.assertFor(expected = false) { it.showUpcomingPhaseDetails }

        viewModel.toggleShowUpcomingPhaseDetails()
        viewModel.lunarCalendarState.assertFor(expected = true) { it.showUpcomingPhaseDetails }
    }
}
