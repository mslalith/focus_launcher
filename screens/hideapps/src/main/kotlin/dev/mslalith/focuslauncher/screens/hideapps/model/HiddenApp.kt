package dev.mslalith.focuslauncher.screens.hideapps.model

import dev.mslalith.focuslauncher.core.model.App

internal data class HiddenApp(
    val app: App,
    val isSelected: Boolean,
    val isFavorite: Boolean
)
