package dev.mslalith.focuslauncher.feature.favorites.model

import androidx.compose.runtime.Immutable
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class FavoritesState(
    val favoritesList: ImmutableList<AppWithColor>,
    val favoritesContextualMode: FavoritesContextMode
)
