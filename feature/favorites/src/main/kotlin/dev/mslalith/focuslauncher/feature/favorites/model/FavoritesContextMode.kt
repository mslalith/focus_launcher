package dev.mslalith.focuslauncher.feature.favorites.model

import dev.mslalith.focuslauncher.core.model.app.App

sealed class FavoritesContextMode {
    data object Open : FavoritesContextMode()
    data object Closed : FavoritesContextMode()
    data object Remove : FavoritesContextMode()
    data object Reorder : FavoritesContextMode()
    data class ReorderPickPosition(val app: App) : FavoritesContextMode()

    fun isInContextualMode(): Boolean = this != Closed
}
