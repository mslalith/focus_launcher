package dev.mslalith.focuslauncher.feature.lunarcalendar.bottomsheet.lunarphasewidgetsettings

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dev.mslalith.focuslauncher.core.common.model.State
import dev.mslalith.focuslauncher.core.model.CurrentPlace
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.UpcomingLunarPhase

data class LunarPhaseWidgetSettingsBottomSheetState(
    val showLunarPhase: Boolean,
    val showIlluminationPercent: Boolean,
    val showUpcomingPhaseDetails: Boolean,
    val lunarPhaseDetails: State<LunarPhaseDetails>,
    val upcomingLunarPhase: State<UpcomingLunarPhase>,
    val currentPlace: CurrentPlace,
    val eventSink: (LunarPhaseWidgetSettingsBottomSheetUiEvent) -> Unit
) : CircuitUiState

sealed interface LunarPhaseWidgetSettingsBottomSheetUiEvent : CircuitUiEvent {
    data object ToggleShowLunarPhase : LunarPhaseWidgetSettingsBottomSheetUiEvent
    data object ToggleShowIlluminationPercent : LunarPhaseWidgetSettingsBottomSheetUiEvent
    data object ToggleShowUpcomingPhaseDetails : LunarPhaseWidgetSettingsBottomSheetUiEvent
    data class Goto(val screen: Screen) : LunarPhaseWidgetSettingsBottomSheetUiEvent
}
