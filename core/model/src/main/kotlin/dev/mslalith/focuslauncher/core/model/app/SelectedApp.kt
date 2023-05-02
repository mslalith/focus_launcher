package dev.mslalith.focuslauncher.core.model.app

data class SelectedApp(
    val app: App,
    val isSelected: Boolean,
    val disabled: Boolean = false
)
