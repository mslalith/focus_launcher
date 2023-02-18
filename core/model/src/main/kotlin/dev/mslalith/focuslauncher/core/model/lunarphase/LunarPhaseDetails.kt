package dev.mslalith.focuslauncher.core.model.lunarphase

import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhase.FIRST_QUARTER
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhase.FULL_MOON
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhase.LAST_QUARTER
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhase.NEW_MOON
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhase.WANING_CRESCENT
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhase.WANING_GIBBOUS
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhase.WAXING_CRESCENT
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhase.WAXING_GIBBOUS

data class LunarPhaseDetails(
    val lunarPhase: LunarPhase,
    val illumination: Double,
    val phaseAngle: Double,
    val nextPhaseDetails: NextPhaseDetails,
    val moonRiseAndSetDetails: RiseAndSetDetails,
    val sunRiseAndSetDetails: RiseAndSetDetails
) {
    val direction: LunarPhaseDirection
        get() = when (lunarPhase) {
            NEW_MOON, WAXING_CRESCENT, FIRST_QUARTER, WAXING_GIBBOUS -> LunarPhaseDirection.NEW_TO_FULL
            FULL_MOON, WANING_GIBBOUS, LAST_QUARTER, WANING_CRESCENT -> LunarPhaseDirection.FULL_TO_NEW
        }
}
