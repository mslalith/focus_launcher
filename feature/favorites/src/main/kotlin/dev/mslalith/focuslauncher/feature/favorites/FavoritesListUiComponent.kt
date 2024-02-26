package dev.mslalith.focuslauncher.feature.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReusableContent
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.slack.circuit.overlay.LocalOverlayHost
import dev.mslalith.focuslauncher.core.circuitoverlay.bottomsheet.showBottomSheet
import dev.mslalith.focuslauncher.core.common.extensions.launchApp
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import dev.mslalith.focuslauncher.core.screens.BottomSheetScreen
import dev.mslalith.focuslauncher.core.screens.FavoritesBottomSheetScreen
import dev.mslalith.focuslauncher.feature.favorites.ui.FavoriteItem
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

    fun showBottomSheet(screen: BottomSheetScreen<Unit>) {
        scope.launch { overlayHost.showBottomSheet(screen) }
    }

    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier.fillMaxWidth()
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
                        onLongClick = { showBottomSheet(screen = FavoritesBottomSheetScreen) }
                    )
                }
            }
        }
    }
}
