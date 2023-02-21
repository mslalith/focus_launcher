package dev.mslalith.focuslauncher.core.model.lunarphase

import kotlinx.datetime.LocalDateTime

data class NextPhaseDetails(
    val newMoon: LocalDateTime?,
    val fullMoon: LocalDateTime?
)
