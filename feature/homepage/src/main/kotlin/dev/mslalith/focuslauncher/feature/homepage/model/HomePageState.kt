package dev.mslalith.focuslauncher.feature.homepage.model

import dev.mslalith.focuslauncher.core.model.App

data class HomePageState(
    val isPullDownNotificationShadeEnabled: Boolean,
    val favoritesContextualMode: FavoritesContextMode,
    val favoritesList: List<App>
)
