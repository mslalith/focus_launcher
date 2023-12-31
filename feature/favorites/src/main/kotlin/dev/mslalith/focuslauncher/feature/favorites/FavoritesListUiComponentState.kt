package dev.mslalith.focuslauncher.feature.favorites

import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import dev.mslalith.focuslauncher.feature.favorites.model.FavoritesContextMode
import kotlinx.collections.immutable.ImmutableList

data class FavoritesListUiComponentState(
    val favoritesList: ImmutableList<AppWithColor>,
    val favoritesContextualMode: FavoritesContextMode,
    val currentTheme: Theme,
    val eventSink: (FavoritesListUiComponentUiEvent) -> Unit
) : CircuitUiState
