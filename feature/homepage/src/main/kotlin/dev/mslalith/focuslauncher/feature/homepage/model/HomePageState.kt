package dev.mslalith.focuslauncher.feature.homepage.model

import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon

data class HomePageState(
    val isPullDownNotificationShadeEnabled: Boolean,
    val favoritesContextualMode: FavoritesContextMode,
    val favoritesList: List<AppWithIcon>
)
