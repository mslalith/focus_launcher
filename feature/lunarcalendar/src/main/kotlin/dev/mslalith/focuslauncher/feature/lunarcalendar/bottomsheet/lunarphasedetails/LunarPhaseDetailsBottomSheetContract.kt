package dev.mslalith.focuslauncher.feature.lunarcalendar.bottomsheet.lunarphasedetails

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.common.model.State
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails

data class LunarPhaseDetailsBottomSheetState(
    val lunarPhaseDetails: State<LunarPhaseDetails>
) : CircuitUiState

sealed interface LunarPhaseDetailsBottomSheetUiEvent : CircuitUiEvent
