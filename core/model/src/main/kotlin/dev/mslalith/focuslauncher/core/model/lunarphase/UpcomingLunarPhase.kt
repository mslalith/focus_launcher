package dev.mslalith.focuslauncher.core.model.lunarphase

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDateTime

@Immutable
data class UpcomingLunarPhase(
    val lunarPhase: LunarPhase,
    val dateTime: LocalDateTime?,
    val isMicroMoon: Boolean,
    val isSuperMoon: Boolean
)
