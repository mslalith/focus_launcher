package dev.mslalith.focuslauncher.core.model.lunarphase

import kotlinx.datetime.LocalDateTime

data class UpcomingLunarPhase(
    val lunarPhase: LunarPhase,
    val dateTime: LocalDateTime?,
    val isMicroMoon: Boolean,
    val isSuperMoon: Boolean
)
