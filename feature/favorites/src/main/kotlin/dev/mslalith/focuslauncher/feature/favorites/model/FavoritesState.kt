package dev.mslalith.focuslauncher.feature.favorites.model

import dev.mslalith.focuslauncher.core.model.app.AppWithColor

internal data class FavoritesState(
    val favoritesList: List<AppWithColor>,
    val favoritesContextualMode: FavoritesContextMode
)
