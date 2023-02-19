package dev.mslalith.focuslauncher.feature.clock24.model

import dev.mslalith.focuslauncher.core.model.ClockAlignment

data class Clock24State(
    val currentTime: String,
    val showClock24: Boolean,
    val clockAlignment: ClockAlignment,
    val clock24AnimationDuration: Int
)
