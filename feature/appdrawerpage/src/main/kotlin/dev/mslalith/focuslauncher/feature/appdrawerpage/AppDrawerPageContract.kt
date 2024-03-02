package dev.mslalith.focuslauncher.feature.appdrawerpage

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.common.model.LoadingState
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerIconViewType
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import kotlinx.collections.immutable.ImmutableList

data class AppDrawerPageState(
    val allAppsState: LoadingState<ImmutableList<AppDrawerItem>>,
    val appDrawerViewType: AppDrawerViewType,
    val appDrawerIconViewType: AppDrawerIconViewType,
    val showAppGroupHeader: Boolean,
    val showSearchBar: Boolean,
    val searchBarQuery: String,
    val eventSink: (AppDrawerPageUiEvent) -> Unit
) : CircuitUiState

sealed interface AppDrawerPageUiEvent : CircuitUiEvent {
    data class UpdateSearchQuery(val query: String) : AppDrawerPageUiEvent
    data object ReloadIconPack : AppDrawerPageUiEvent
}
