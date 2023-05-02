package dev.mslalith.focuslauncher.core.model.lunarphase

import dev.mslalith.focuslauncher.core.model.R
import dev.mslalith.focuslauncher.core.model.UiText
import org.shredzone.commons.suncalc.MoonPhase

enum class LunarPhase(val phaseNameUiText: UiText) {
    NEW_MOON(phaseNameUiText = UiText.Resource(stringRes = R.string.new_moon)),
    WAXING_CRESCENT(phaseNameUiText = UiText.Resource(stringRes = R.string.waxing_crescent)),
    FIRST_QUARTER(phaseNameUiText = UiText.Resource(stringRes = R.string.first_quarter)),
    WAXING_GIBBOUS(phaseNameUiText = UiText.Resource(stringRes = R.string.waxing_gibbous)),
    FULL_MOON(phaseNameUiText = UiText.Resource(stringRes = R.string.full_moon)),
    WANING_GIBBOUS(phaseNameUiText = UiText.Resource(stringRes = R.string.waning_gibbous)),
    LAST_QUARTER(phaseNameUiText = UiText.Resource(stringRes = R.string.last_quarter)),
    WANING_CRESCENT(phaseNameUiText = UiText.Resource(stringRes = R.string.waning_crescent));

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
