package dev.mslalith.focuslauncher.core.model.lunarphase

import kotlinx.datetime.LocalDateTime

data class RiseAndSetDetails(
    val riseDateTime: LocalDateTime?,
    val setDateTime: LocalDateTime?
)
