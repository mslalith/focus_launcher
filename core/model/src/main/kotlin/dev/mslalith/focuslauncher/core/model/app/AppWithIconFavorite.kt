package dev.mslalith.focuslauncher.core.model.app

import androidx.compose.runtime.Immutable

@Immutable
data class AppWithIconFavorite(
    val appWithIcon: AppWithIcon,
    val isFavorite: Boolean
)
