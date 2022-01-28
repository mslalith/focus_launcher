package dev.mslalith.focuslauncher.data.models

import dev.mslalith.focuslauncher.data.models.LunarPhase.FIRST_QUARTER
import dev.mslalith.focuslauncher.data.models.LunarPhase.FULL_MOON
import dev.mslalith.focuslauncher.data.models.LunarPhase.LAST_QUARTER
import dev.mslalith.focuslauncher.data.models.LunarPhase.NEW_MOON
import dev.mslalith.focuslauncher.data.models.LunarPhase.WANING_CRESCENT
import dev.mslalith.focuslauncher.data.models.LunarPhase.WANING_GIBBOUS
import dev.mslalith.focuslauncher.data.models.LunarPhase.WAXING_CRESCENT
import dev.mslalith.focuslauncher.data.models.LunarPhase.WAXING_GIBBOUS
import dev.mslalith.focuslauncher.data.models.LunarPhaseDirection.FULL_TO_NEW
import dev.mslalith.focuslauncher.data.models.LunarPhaseDirection.NEW_TO_FULL
import kotlinx.datetime.LocalDateTime
import org.shredzone.commons.suncalc.MoonPhase

data class LunarPhaseDetails(
    val lunarPhase: LunarPhase,
    val illumination: Double,
    val phaseAngle: Double,
) {
    val direction: LunarPhaseDirection
        get() = when (lunarPhase) {
            NEW_MOON, WAXING_CRESCENT, FIRST_QUARTER, WAXING_GIBBOUS -> NEW_TO_FULL
            FULL_MOON, WANING_GIBBOUS, LAST_QUARTER, WANING_CRESCENT -> FULL_TO_NEW
        }
}

data class UpcomingLunarPhase(
    val lunarPhase: LunarPhase,
    val dateTime: LocalDateTime?,
    val isMicroMoon: Boolean,
    val isSuperMoon: Boolean,
)

enum class LunarPhaseDirection {
    NEW_TO_FULL,
    FULL_TO_NEW,
}

enum class LunarPhase(val phaseName: String) {
    NEW_MOON("New Moon"),
    WAXING_CRESCENT("Waxing Crescent"),
    FIRST_QUARTER("First Quarter"),
    WAXING_GIBBOUS("Waxing Gibbous"),
    FULL_MOON("Full Moon"),
    WANING_GIBBOUS("Waning Gibbous"),
    LAST_QUARTER("Last Quarter"),
    WANING_CRESCENT("Waning Crescent");

    fun toMoonPhase() = when (this) {
        NEW_MOON -> MoonPhase.Phase.NEW_MOON
        WAXING_CRESCENT -> MoonPhase.Phase.WAXING_CRESCENT
        FIRST_QUARTER -> MoonPhase.Phase.FIRST_QUARTER
        WAXING_GIBBOUS -> MoonPhase.Phase.WAXING_GIBBOUS
        FULL_MOON -> MoonPhase.Phase.FULL_MOON
        WANING_GIBBOUS -> MoonPhase.Phase.WANING_GIBBOUS
        LAST_QUARTER -> MoonPhase.Phase.LAST_QUARTER
        WANING_CRESCENT -> MoonPhase.Phase.WANING_CRESCENT
    }
}

fun MoonPhase.Phase.toLunarPhase() = when (this) {
    MoonPhase.Phase.NEW_MOON -> NEW_MOON
    MoonPhase.Phase.WAXING_CRESCENT -> WAXING_CRESCENT
    MoonPhase.Phase.FIRST_QUARTER -> FIRST_QUARTER
    MoonPhase.Phase.WAXING_GIBBOUS -> WAXING_GIBBOUS
    MoonPhase.Phase.FULL_MOON -> FULL_MOON
    MoonPhase.Phase.WANING_GIBBOUS -> WANING_GIBBOUS
    MoonPhase.Phase.LAST_QUARTER -> LAST_QUARTER
    MoonPhase.Phase.WANING_CRESCENT -> WANING_CRESCENT
}
