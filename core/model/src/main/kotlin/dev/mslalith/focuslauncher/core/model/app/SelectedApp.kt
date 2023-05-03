package dev.mslalith.focuslauncher.core.model.app

import androidx.compose.runtime.Immutable

@Immutable
data class SelectedApp(
    val app: App,
    val isSelected: Boolean,
    val disabled: Boolean = false
)
