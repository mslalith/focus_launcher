package dev.mslalith.focuslauncher.feature.favorites.bottomsheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import dev.mslalith.focuslauncher.core.screens.FavoritesBottomSheetScreen
import dev.mslalith.focuslauncher.core.ui.ActionButton
import dev.mslalith.focuslauncher.core.ui.HorizontalSpacer
import dev.mslalith.focuslauncher.core.ui.RoundIcon
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.feature.favorites.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyListState
import sh.calvin.reorderable.rememberReorderableLazyColumnState

@CircuitInject(FavoritesBottomSheetScreen::class, SingletonComponent::class)
@Composable
fun FavoritesBottomSheet(
    state: FavoritesBottomSheetState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink


    FavoritesBottomSheet(
        modifier = modifier,
        favorites = state.favoritesList,
        onMove = { from, to -> eventSink(FavoritesBottomSheetUiEvent.Move(fromIndex = from, toIndex = to)) },
        onSaveClick = { eventSink(FavoritesBottomSheetUiEvent.Save) },
        onRemoveFavorite = { eventSink(FavoritesBottomSheetUiEvent.Remove(appWithColor = it)) }
    )
}

@Composable
private fun FavoritesBottomSheet(
    favorites: ImmutableList<AppWithColor>,
    onMove: (Int, Int) -> Unit,
    onSaveClick: () -> Unit,
    onRemoveFavorite: (AppWithColor) -> Unit,
    modifier: Modifier = Modifier
) {
    var showInfoContent by remember { mutableStateOf(value = false) }

    val lazyListState = rememberLazyListState()
    val reorderableLazyColumnState = rememberReorderableLazyColumnState(lazyListState = lazyListState) { from, to ->
        onMove(from.index, to.index)
    }

    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Header(
            onInfoClick = { showInfoContent = !showInfoContent }
        )

        VerticalSpacer(spacing = 12.dp)

        AnimatedVisibility(
            visible = showInfoContent
        ) {
            Column {
                InfoContent()
                VerticalSpacer(spacing = 24.dp)
            }
        }

        FavoritesList(
            modifier = Modifier.weight(weight = 1f, fill = false),
            lazyListState = lazyListState,
            reorderableLazyColumnState = reorderableLazyColumnState,
            favorites = favorites,
            onRemoveFavorite = onRemoveFavorite
        )

        VerticalSpacer(spacing = 12.dp)

        ActionButton(
            text = stringResource(id = R.string.save),
            onClick = onSaveClick
        )

        VerticalSpacer(spacing = 12.dp)
    }
}

@Composable
private fun Header(
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.favorites),
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(weight = 1f)
        )
        RoundIcon(
            iconRes = R.drawable.ic_info,
            onClick = onInfoClick,
            backgroundColor = Color.Transparent
        )
    }
}

@Composable
private fun InfoContent(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_hand_swipe_left),
            contentDescription = null
        )
        HorizontalSpacer(spacing = 12.dp)
        Text(text = stringResource(id = R.string.to_remove_favorite))
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun FavoritesList(
    lazyListState: LazyListState,
    reorderableLazyColumnState: ReorderableLazyListState,
    favorites: ImmutableList<AppWithColor>,
    onRemoveFavorite: (AppWithColor) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        items(
            items = favorites,
            key = { it.app.packageName }
        ) { appWithColor ->
            ReorderableItem(
                state = reorderableLazyColumnState,
                key = appWithColor.app.packageName
            ) { isDragging ->
                DismissibleFavoriteItem(
                    appWithColor = appWithColor,
                    isDragging = isDragging,
                    onDismiss = onRemoveFavorite
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewInfoContent() {
    MaterialTheme {
        Surface {
            FavoritesBottomSheet(
                favorites = persistentListOf(
                    AppWithColor(
                        app = App(name = "", displayName = "Chrome", packageName = "", isSystem = false),
                        color = null
                    )
                ),
                onMove = { _, _ -> },
                onRemoveFavorite = {},
                onSaveClick = {}
            )
        }
    }
}
