package dev.mslalith.focuslauncher.feature.homepage.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReusableContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import dev.mslalith.focuslauncher.core.common.extensions.defaultDialerApp
import dev.mslalith.focuslauncher.core.common.extensions.defaultMessagingApp
import dev.mslalith.focuslauncher.core.common.extensions.launchApp
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.ui.BackPressHandler
import dev.mslalith.focuslauncher.core.ui.extensions.toAppWithIconList
import dev.mslalith.focuslauncher.feature.homepage.model.FavoritesContextMode

@Composable
internal fun FavoritesList(
    favoritesList: List<App>,
    addDefaultAppsToFavorites: (List<App>) -> Unit,
    removeFromFavorites: (App) -> Unit,
    reorderFavorite: (App, App, () -> Unit) -> Unit,
    currentContextMode1: FavoritesContextMode,
    isInContextualMode: () -> Boolean,
    isReordering: () -> Boolean,
    hideContextualMode: () -> Unit,
    changeFavoritesContextMode: (FavoritesContextMode) -> Unit,
    isAppAboutToReorder: (App) -> Boolean,
    contentPadding: Dp
) {
    val context = LocalContext.current
    val currentContextMode by rememberUpdatedState(newValue = currentContextMode1)
    val favoritesWithAppIcon = remember(key1 = favoritesList) {
        // derivedStateOf {
        favoritesList.toAppWithIconList(context)
        // }
    }

    LaunchedEffect(favoritesWithAppIcon.isEmpty()) {
        if (favoritesWithAppIcon.isNotEmpty() || isReordering()) return@LaunchedEffect

        hideContextualMode()
        val defaultApps = listOfNotNull(context.defaultDialerApp, context.defaultMessagingApp)
        if (defaultApps.isNotEmpty()) addDefaultAppsToFavorites(defaultApps)
    }

    BackPressHandler(enabled = isInContextualMode()) { hideContextualMode() }

    val transition = updateTransition(targetState = currentContextMode, label = "Favorites Transition")
    val outerPadding by transition.animateDp(label = "Outer Padding") { if (it.isInContextualMode()) 16.dp else 0.dp }
    val innerPaddingBottom by transition.animateDp(label = "Inner Padding Bottom") { if (it.isInContextualMode()) 16.dp else 0.dp }
    val borderOpacity by transition.animateFloat(label = "Border Opacity") { if (it.isInContextualMode()) 0.8f else 0f }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = outerPadding)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onBackground.copy(alpha = borderOpacity),
                shape = RoundedCornerShape(size = 12.dp)
            )
            .padding(bottom = innerPaddingBottom)
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedVisibility(
                visible = isInContextualMode(),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                FavoritesContextHeader(
                    currentContextMode = currentContextMode,
                    changeContextModeToOpen = { changeFavoritesContextMode(FavoritesContextMode.Open) },
                    onReorderClick = { changeFavoritesContextMode(FavoritesContextMode.Reorder) },
                    onRemoveClick = { changeFavoritesContextMode(FavoritesContextMode.Remove) }
                )
            }
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = contentPadding),
                mainAxisSpacing = 16.dp,
                crossAxisSpacing = 12.dp
            ) {
                favoritesWithAppIcon.forEach { favorite ->
                    ReusableContent(key = favorite) {
                        FavoriteItem(
                            app = favorite,
                            isInContextualMode = isInContextualMode,
                            isAppAboutToReorder = { isAppAboutToReorder(favorite.toApp()) },
                            changeFavoritesContextMode = changeFavoritesContextMode,
                            onClick = {
                                when (currentContextMode) {
                                    FavoritesContextMode.Closed -> context.launchApp(favorite.toApp())
                                    FavoritesContextMode.Open -> Unit
                                    FavoritesContextMode.Remove -> removeFromFavorites(favorite.toApp())
                                    FavoritesContextMode.Reorder -> changeFavoritesContextMode(FavoritesContextMode.ReorderPickPosition(favorite.toApp()))
                                    is FavoritesContextMode.ReorderPickPosition -> {
                                        val reorderPickPosition = currentContextMode as FavoritesContextMode.ReorderPickPosition
                                        reorderFavorite(reorderPickPosition.app, favorite.toApp()) {
                                            changeFavoritesContextMode(FavoritesContextMode.Reorder)
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
