package dev.mslalith.focuslauncher.core.model.lunarphase

import org.shredzone.commons.suncalc.MoonPhase

enum class LunarPhase(val phaseName: String) {
    NEW_MOON(phaseName = "New Moon"),
    WAXING_CRESCENT(phaseName = "Waxing Crescent"),
    FIRST_QUARTER(phaseName = "First Quarter"),
    WAXING_GIBBOUS(phaseName = "Waxing Gibbous"),
    FULL_MOON(phaseName = "Full Moon"),
    WANING_GIBBOUS(phaseName = "Waning Gibbous"),
    LAST_QUARTER(phaseName = "Last Quarter"),
    WANING_CRESCENT(phaseName = "Waning Crescent");

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
    MoonPhase.Phase.NEW_MOON -> LunarPhase.NEW_MOON
    MoonPhase.Phase.WAXING_CRESCENT -> LunarPhase.WAXING_CRESCENT
    MoonPhase.Phase.FIRST_QUARTER -> LunarPhase.FIRST_QUARTER
    MoonPhase.Phase.WAXING_GIBBOUS -> LunarPhase.WAXING_GIBBOUS
    MoonPhase.Phase.FULL_MOON -> LunarPhase.FULL_MOON
    MoonPhase.Phase.WANING_GIBBOUS -> LunarPhase.WANING_GIBBOUS
    MoonPhase.Phase.LAST_QUARTER -> LunarPhase.LAST_QUARTER
    MoonPhase.Phase.WANING_CRESCENT -> LunarPhase.WANING_CRESCENT
}
