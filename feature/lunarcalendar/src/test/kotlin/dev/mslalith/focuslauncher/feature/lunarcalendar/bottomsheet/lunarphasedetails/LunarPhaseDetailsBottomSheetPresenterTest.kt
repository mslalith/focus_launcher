package dev.mslalith.focuslauncher.feature.lunarcalendar.bottomsheet.lunarphasedetails

import dev.mslalith.focuslauncher.core.common.model.getOrNull
import dev.mslalith.focuslauncher.core.data.test.repository.FakeLunarPhaseDetailsRepo
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhase
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.NextPhaseDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.RiseAndSetDetails
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import kotlinx.datetime.LocalDateTime
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AppRobolectricTestRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class LunarPhaseDetailsBottomSheetPresenterTest : PresenterTest<LunarPhaseDetailsBottomSheetPresenter, LunarPhaseDetailsBottomSheetState>() {

    private val lunarPhaseDetailsRepo = FakeLunarPhaseDetailsRepo()

    override fun presenterUnderTest() = LunarPhaseDetailsBottomSheetPresenter(
        lunarPhaseDetailsRepo = lunarPhaseDetailsRepo
    )

    @Test
    fun `01 - verify initial state`() {
        val expectedLunarPhaseDetails = LunarPhaseDetails(
            lunarPhase = LunarPhase.FIRST_QUARTER,
            illumination = 0.499714382113712,
            phaseAngle = -90.03272940065548,
            nextPhaseDetails = NextPhaseDetails(
                newMoon = LocalDateTime.parse("2023-03-21T17:25:46.771"),
                fullMoon = LocalDateTime.parse("2023-03-07T12:39:47.981")
            ),
            moonRiseAndSetDetails = RiseAndSetDetails(
                riseDateTime = LocalDateTime.parse("2023-02-27T12:05:52"),
                setDateTime = LocalDateTime.parse("2023-02-28T00:30:33")
            ),
            sunRiseAndSetDetails = RiseAndSetDetails(
                riseDateTime = LocalDateTime.parse("2023-02-27T06:09:24"),
                setDateTime = LocalDateTime.parse("2023-02-27T18:16:07")
            )
        )
        lunarPhaseDetailsRepo.setLunarPhaseDetails(expectedLunarPhaseDetails)

        runPresenterTest {
            assertFor(expected = expectedLunarPhaseDetails) { it.lunarPhaseDetails.getOrNull() }
        }
    }
}
