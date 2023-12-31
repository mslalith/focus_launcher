package dev.mslalith.focuslauncher.feature.lunarcalendar.widget

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.data.test.repository.FakeClockRepo
import dev.mslalith.focuslauncher.core.data.test.repository.FakeLunarPhaseDetailsRepo
import dev.mslalith.focuslauncher.core.data.test.repository.settings.FakeLunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AppRobolectricTestRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class LunarCalendarUiComponentPresenterTest : PresenterTest<LunarCalendarUiComponentPresenter, LunarCalendarUiComponentState>() {

    private val clockRepo = FakeClockRepo()
    private val lunarPhaseDetailsRepo = FakeLunarPhaseDetailsRepo()
    private val lunarPhaseSettingsRepo = FakeLunarPhaseSettingsRepo()

    override fun presenterUnderTest() = LunarCalendarUiComponentPresenter(
        clockRepo = clockRepo,
        lunarPhaseDetailsRepo = lunarPhaseDetailsRepo,
        lunarPhaseSettingsRepo = lunarPhaseSettingsRepo
    )

    @Test
    fun `01 - on toggle lunar phase visibility, verify state change`() = runPresenterTest {
        assertThat(awaitItem().showLunarPhase).isTrue()

        lunarPhaseSettingsRepo.toggleShowLunarPhase()
        assertFor(expected = false) { it.showLunarPhase }

        lunarPhaseSettingsRepo.toggleShowLunarPhase()
        assertFor(expected = true) { it.showLunarPhase }
    }

    @Test
    fun `02 - on toggle lunar illumination visibility, verify state change`() = runPresenterTest {
        assertThat(awaitItem().showIlluminationPercent).isTrue()

        lunarPhaseSettingsRepo.toggleShowIlluminationPercent()
        assertFor(expected = false) { it.showIlluminationPercent }

        lunarPhaseSettingsRepo.toggleShowIlluminationPercent()
        assertFor(expected = true) { it.showIlluminationPercent }
    }

    @Test
    fun `03 - on toggle upcoming lunar visibility, verify state change`() = runPresenterTest {
        assertThat(awaitItem().showUpcomingPhaseDetails).isTrue()

        lunarPhaseSettingsRepo.toggleShowUpcomingPhaseDetails()
        assertFor(expected = false) { it.showUpcomingPhaseDetails }

        lunarPhaseSettingsRepo.toggleShowUpcomingPhaseDetails()
        assertFor(expected = true) { it.showUpcomingPhaseDetails }
    }
}
