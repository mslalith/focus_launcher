package dev.mslalith.focuslauncher.core.model.app

import androidx.compose.runtime.Immutable

@Immutable
data class SelectedHiddenApp(
    val app: App,
    val isSelected: Boolean,
    val isFavorite: Boolean
)
