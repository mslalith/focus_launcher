package dev.mslalith.focuslauncher.screens.iconpack

import com.slack.circuit.runtime.CircuitUiEvent
import dev.mslalith.focuslauncher.core.model.IconPackType

sealed interface IconPackUiEvent : CircuitUiEvent {
    data object GoBack : IconPackUiEvent
    data object SaveIconPack : IconPackUiEvent
    data class UpdateSelectedIconPackApp(val iconPackType: IconPackType) : IconPackUiEvent
}
