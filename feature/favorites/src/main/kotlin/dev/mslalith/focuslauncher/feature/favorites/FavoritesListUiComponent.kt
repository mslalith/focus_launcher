package dev.mslalith.focuslauncher.feature.favorites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReusableContent
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.slack.circuit.overlay.LocalOverlayHost
import dev.mslalith.focuslauncher.core.circuitoverlay.bottomsheet.showBottomSheet
import dev.mslalith.focuslauncher.core.common.extensions.launchApp
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import dev.mslalith.focuslauncher.core.screens.BottomSheetScreen
import dev.mslalith.focuslauncher.core.screens.FavoritesBottomSheetScreen
import dev.mslalith.focuslauncher.core.ui.remember.rememberAppColor
import dev.mslalith.focuslauncher.feature.favorites.ui.StaggeredFlowRow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

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
        modifier = modifier,
        favoritesList = state.favoritesList,
        addDefaultAppsToFavorites = { eventSink(FavoritesListUiComponentUiEvent.AddDefaultAppsIfRequired) },
        contentPadding = contentPadding
    )
}

@Composable
private fun FavoritesListUiComponent(
    favoritesList: ImmutableList<AppWithColor>,
    addDefaultAppsToFavorites: () -> Unit,
    contentPadding: Dp,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val overlayHost = LocalOverlayHost.current

    LaunchedEffect(key1 = favoritesList.isEmpty()) {
        if (favoritesList.isNotEmpty()) return@LaunchedEffect

        addDefaultAppsToFavorites()
    }

    fun showBottomSheet(
        screen: BottomSheetScreen<Unit>,
        skipPartiallyExpanded: Boolean = true
    ) {
        scope.launch {
            overlayHost.showBottomSheet(
                screen = screen,
                skipPartiallyExpanded = skipPartiallyExpanded
            )
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
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
                        onClick = { context.launchApp(app = favoriteAppWithColor.app) },
                        onLongClick = { showBottomSheet(screen = FavoritesBottomSheetScreen, skipPartiallyExpanded = false) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FavoriteItem(
    appWithColor: AppWithColor,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appIconBasedColor = rememberAppColor(graphicsColor = appWithColor.color)

    Row(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = appIconBasedColor.copy(alpha = 0.23f))
            .border(
                width = 0.4.dp,
                color = appIconBasedColor.copy(alpha = 0.6f),
                shape = MaterialTheme.shapes.small
            )
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = appWithColor.app.displayName,
            color = appIconBasedColor,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
