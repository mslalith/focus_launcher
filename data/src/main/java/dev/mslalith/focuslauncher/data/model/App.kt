package dev.mslalith.focuslauncher.data.model

import dev.mslalith.focuslauncher.core.model.App

data class SelectedApp(
    val app: App,
    val isSelected: Boolean,
    val disabled: Boolean = false
)
