package dev.mslalith.focuslauncher.screens.hideapps.model

import androidx.compose.runtime.Immutable
import dev.mslalith.focuslauncher.core.model.app.SelectedHiddenApp
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class HideAppsState(
    val hiddenApps: ImmutableList<SelectedHiddenApp>
)
