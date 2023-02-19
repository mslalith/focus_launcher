package dev.mslalith.focuslauncher.feature.lunarcalendar.model

import dev.mslalith.focuslauncher.core.common.State
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.UpcomingLunarPhase

internal data class LunarCalendarState(
    val showLunarPhase: Boolean,
    val showIlluminationPercent: Boolean,
    val showUpcomingPhaseDetails: Boolean,
    val lunarPhaseDetails: State<LunarPhaseDetails>,
    val upcomingLunarPhase: State<UpcomingLunarPhase>
)
