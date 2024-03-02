package dev.mslalith.focuslauncher.feature.lunarcalendar.widget

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.common.model.State
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.UpcomingLunarPhase

data class LunarCalendarUiComponentState(
    val showLunarPhase: Boolean,
    val showIlluminationPercent: Boolean,
    val showUpcomingPhaseDetails: Boolean,
    val lunarPhaseDetails: State<LunarPhaseDetails>,
    val upcomingLunarPhase: State<UpcomingLunarPhase>
) : CircuitUiState

sealed interface LunarCalendarUiComponentUiEvent : CircuitUiEvent
