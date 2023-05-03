package dev.mslalith.focuslauncher.core.model.lunarphase

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDateTime

@Immutable
data class NextPhaseDetails(
    val newMoon: LocalDateTime?,
    val fullMoon: LocalDateTime?
)
