package dev.mslalith.focuslauncher.feature.favorites.model

import dev.mslalith.focuslauncher.core.model.App

internal sealed class FavoritesContextMode {
    object Open : FavoritesContextMode()
    object Closed : FavoritesContextMode()
    object Remove : FavoritesContextMode()
    object Reorder : FavoritesContextMode()
    data class ReorderPickPosition(val app: App) : FavoritesContextMode()

    fun isInContextualMode(): Boolean = this != Closed
}
