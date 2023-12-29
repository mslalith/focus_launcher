package dev.mslalith.focuslauncher.feature.appdrawerpage

import com.slack.circuit.runtime.CircuitUiEvent

sealed interface AppDrawerPageUiEvent : CircuitUiEvent {
    data class UpdateSearchQuery(val query: String) : AppDrawerPageUiEvent
    data object ReloadIconPack : AppDrawerPageUiEvent
}
