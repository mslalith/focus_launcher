package dev.mslalith.focuslauncher.feature.lunarcalendar.bottomsheet.lunarphasewidgetsettings

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.screen.Screen

sealed interface LunarPhaseWidgetSettingsBottomSheetUiEvent : CircuitUiEvent {
    data object ToggleShowLunarPhase : LunarPhaseWidgetSettingsBottomSheetUiEvent
    data object ToggleShowIlluminationPercent : LunarPhaseWidgetSettingsBottomSheetUiEvent
    data object ToggleShowUpcomingPhaseDetails : LunarPhaseWidgetSettingsBottomSheetUiEvent
    data class Goto(val screen: Screen) : LunarPhaseWidgetSettingsBottomSheetUiEvent
}
