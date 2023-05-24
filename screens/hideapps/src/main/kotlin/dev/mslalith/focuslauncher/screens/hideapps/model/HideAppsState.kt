package dev.mslalith.focuslauncher.screens.hideapps.model

import dev.mslalith.focuslauncher.core.model.app.SelectedHiddenApp
import kotlinx.collections.immutable.ImmutableList

data class HideAppsState(
    val hiddenApps: ImmutableList<SelectedHiddenApp>
)
