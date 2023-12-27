package dev.mslalith.focuslauncher.feature.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReusableContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.common.extensions.launchApp
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import dev.mslalith.focuslauncher.core.ui.BackPressHandler
import dev.mslalith.focuslauncher.feature.favorites.model.FavoritesContextMode
import dev.mslalith.focuslauncher.feature.favorites.ui.FavoriteItem
import dev.mslalith.focuslauncher.feature.favorites.ui.FavoritesContextHeader
import dev.mslalith.focuslauncher.feature.favorites.ui.StaggeredFlowRow
import kotlinx.collections.immutable.ImmutableList

@Composable
fun FavoritesListUiComponent(
    state: FavoritesListUiComponentState,
    contentPadding: Dp,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    FavoritesListUiComponent(
        favoritesList = state.favoritesList,
        addDefaultAppsToFavorites = { eventSink(FavoritesListUiComponentUiEvent.AddDefaultAppsIfRequired) },
        removeFromFavorites = { eventSink(FavoritesListUiComponentUiEvent.RemoveFromFavorites(app = it)) },
        reorderFavorite = { app, withApp, onReordered -> eventSink(FavoritesListUiComponentUiEvent.ReorderFavorite(app = app, withApp = withApp, onReordered = onReordered)) },
        contextMode = state.favoritesContextualMode,
        isInContextualMode = { state.favoritesContextualMode != FavoritesContextMode.Closed },
        isReordering = {
            when (state.favoritesContextualMode) {
                FavoritesContextMode.Reorder, is FavoritesContextMode.ReorderPickPosition -> true
                else -> false
            }
        },
        hideContextualMode = { eventSink(FavoritesListUiComponentUiEvent.UpdateFavoritesContextMode(mode = FavoritesContextMode.Closed)) },
        changeFavoritesContextMode = { eventSink(FavoritesListUiComponentUiEvent.UpdateFavoritesContextMode(mode = it)) },
        isAppAboutToReorder = { app ->
            if (state.favoritesContextualMode is FavoritesContextMode.ReorderPickPosition) {
                state.favoritesContextualMode.app.packageName != app.packageName
            } else {
                false
            }
        },
        contentPadding = contentPadding
    )
}

@Composable
private fun FavoritesListUiComponent(
    favoritesList: ImmutableList<AppWithColor>,
    addDefaultAppsToFavorites: () -> Unit,
    removeFromFavorites: (App) -> Unit,
    reorderFavorite: (App, App, () -> Unit) -> Unit,
    contextMode: FavoritesContextMode,
    isInContextualMode: () -> Boolean,
    isReordering: () -> Boolean,
    hideContextualMode: () -> Unit,
    changeFavoritesContextMode: (FavoritesContextMode) -> Unit,
    isAppAboutToReorder: (App) -> Boolean,
    contentPadding: Dp
) {
    val context = LocalContext.current
    val currentContextMode by rememberUpdatedState(newValue = contextMode)

    LaunchedEffect(key1 = favoritesList.isEmpty()) {
        if (favoritesList.isNotEmpty() || isReordering()) return@LaunchedEffect

        hideContextualMode()
        addDefaultAppsToFavorites()
    }

    BackPressHandler(enabled = isInContextualMode()) { hideContextualMode() }

    val transition = updateTransition(targetState = currentContextMode, label = "Favorites Transition")
    val outerPadding by transition.animateDp(label = "Outer Padding") { if (it.isInContextualMode()) 16.dp else 0.dp }
    val innerPaddingBottom by transition.animateDp(label = "Inner Padding Bottom") { if (it.isInContextualMode()) 24.dp else 0.dp }
    val borderOpacity by transition.animateFloat(label = "Border Opacity") { if (it.isInContextualMode()) 0.8f else 0f }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = outerPadding)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = borderOpacity),
                shape = MaterialTheme.shapes.extraLarge
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
            StaggeredFlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = contentPadding),
                mainAxisSpacing = 16.dp,
                crossAxisSpacing = 12.dp
            ) {
                favoritesList.forEach { favoriteAppWithColor ->
                    ReusableContent(key = favoriteAppWithColor.app) {
                        FavoriteItem(
                            appWithColor = favoriteAppWithColor,
                            isInContextualMode = isInContextualMode,
                            isAppAboutToReorder = { isAppAboutToReorder(favoriteAppWithColor.app) },
                            changeFavoritesContextMode = changeFavoritesContextMode,
                            onClick = {
                                when (currentContextMode) {
                                    FavoritesContextMode.Closed -> context.launchApp(app = favoriteAppWithColor.app)
                                    FavoritesContextMode.Open -> Unit
                                    FavoritesContextMode.Remove -> removeFromFavorites(favoriteAppWithColor.app)
                                    FavoritesContextMode.Reorder -> changeFavoritesContextMode(FavoritesContextMode.ReorderPickPosition(favoriteAppWithColor.app))
                                    is FavoritesContextMode.ReorderPickPosition -> {
                                        val reorderPickPosition = currentContextMode as FavoritesContextMode.ReorderPickPosition
                                        reorderFavorite(reorderPickPosition.app, favoriteAppWithColor.app) {
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
