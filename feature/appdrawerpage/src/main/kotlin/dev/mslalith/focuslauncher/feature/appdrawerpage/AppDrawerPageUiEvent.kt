package dev.mslalith.focuslauncher.feature.appdrawerpage

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.screen.Screen

sealed interface AppDrawerPageUiEvent : CircuitUiEvent {
    data class UpdateSearchQuery(val query: String) : AppDrawerPageUiEvent
    data object ReloadIconPack : AppDrawerPageUiEvent
    data class OpenBottomSheet(val screen: Screen) : AppDrawerPageUiEvent
}
