package dev.mslalith.focuslauncher.feature.appdrawerpage.bottomsheet.moreoptions

import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem

data class AppMoreOptionsBottomSheetState(
    val appDrawerItem: AppDrawerItem,
    val eventSink: (AppMoreOptionsBottomSheetUiEvent) -> Unit
) : CircuitUiState
