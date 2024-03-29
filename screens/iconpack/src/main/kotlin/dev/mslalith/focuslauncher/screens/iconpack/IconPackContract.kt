package dev.mslalith.focuslauncher.screens.iconpack

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.common.model.LoadingState
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import kotlinx.collections.immutable.ImmutableList

data class IconPackState(
    val allApps: LoadingState<ImmutableList<AppDrawerItem>>,
    val iconPacks: ImmutableList<AppWithIcon>,
    val iconPackType: IconPackType?,
    val canSave: Boolean,
    val eventSink: (IconPackUiEvent) -> Unit
) : CircuitUiState

sealed interface IconPackUiEvent : CircuitUiEvent {
    data object GoBack : IconPackUiEvent
    data object SaveIconPack : IconPackUiEvent
    data class UpdateSelectedIconPackApp(val iconPackType: IconPackType) : IconPackUiEvent
}
