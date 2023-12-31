package dev.mslalith.focuslauncher.feature.settingspage.bottomsheet.appdrawersettings

import com.slack.circuit.runtime.CircuitUiEvent
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerIconViewType

sealed interface AppDrawerSettingsBottomSheetUiEvent : CircuitUiEvent {
    data object ToggleSearchBarVisibility : AppDrawerSettingsBottomSheetUiEvent
    data object ToggleAppGroupHeaderVisibility : AppDrawerSettingsBottomSheetUiEvent
    data class UpdateAppDrawerViewType(val viewType: AppDrawerViewType) : AppDrawerSettingsBottomSheetUiEvent
    data class UpdateAppDrawerIconViewType(val viewType: AppDrawerIconViewType) : AppDrawerSettingsBottomSheetUiEvent
}
