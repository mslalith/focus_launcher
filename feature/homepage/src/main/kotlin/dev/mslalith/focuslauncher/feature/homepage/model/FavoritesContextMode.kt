package dev.mslalith.focuslauncher.feature.homepage.model

import dev.mslalith.focuslauncher.core.model.App

sealed class FavoritesContextMode {
    object Open : FavoritesContextMode()
    object Closed : FavoritesContextMode()
    object Remove : FavoritesContextMode()
    object Reorder : FavoritesContextMode()
    data class ReorderPickPosition(val app: App) : FavoritesContextMode()

    fun isInContextualMode(): Boolean = this != Closed
}
