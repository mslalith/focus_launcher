package dev.mslalith.focuslauncher.screens.hideapps

import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.model.app.SelectedHiddenApp
import kotlinx.collections.immutable.ImmutableList

data class HideAppsState(
    val hiddenApps: ImmutableList<SelectedHiddenApp>,
    val eventSink: (HideAppsUiEvent) -> Unit
) : CircuitUiState
