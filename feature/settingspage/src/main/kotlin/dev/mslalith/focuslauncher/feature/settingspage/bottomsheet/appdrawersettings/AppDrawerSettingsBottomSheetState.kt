package dev.mslalith.focuslauncher.feature.settingspage.bottomsheet.appdrawersettings

import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerIconViewType

data class AppDrawerSettingsBottomSheetState(
    val appDrawerViewType: AppDrawerViewType,
    val appDrawerIconViewType: AppDrawerIconViewType,
    val showAppGroupHeader: Boolean,
    val showSearchBar: Boolean,
    val eventSink: (AppDrawerSettingsBottomSheetUiEvent) -> Unit
) : CircuitUiState
