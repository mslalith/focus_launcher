package dev.mslalith.focuslauncher.core.model

data class SelectedApp(
    val app: App,
    val isSelected: Boolean,
    val disabled: Boolean = false
)
