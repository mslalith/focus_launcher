package dev.mslalith.focuslauncher.feature.favorites.bottomsheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import dev.mslalith.focuslauncher.feature.favorites.R
import kotlinx.coroutines.delay
import sh.calvin.reorderable.ReorderableCollectionItemScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReorderableCollectionItemScope.DismissibleFavoriteItem(
    appWithColor: AppWithColor,
    isDragging: Boolean,
    onDismiss: (AppWithColor) -> Unit,
    modifier: Modifier = Modifier,
    dismissAnimationDuration: Int = 500
) {
    var isRemoved by remember { mutableStateOf(value = false) }

    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        positionalThreshold = with(LocalDensity.current) {
            { 56.dp.toPx() }
        },
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) isRemoved = true
            true
        }
    )

    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(timeMillis = dismissAnimationDuration.toLong())
            onDismiss(appWithColor)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = dismissAnimationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            modifier = modifier,
            state = swipeToDismissBoxState,
            enableDismissFromEndToStart = true,
            enableDismissFromStartToEnd = false,
            backgroundContent = {
                val contentColor = MaterialTheme.colorScheme.onError
                val backgroundColor by animateColorAsState(
                    label = "Color animation",
                    targetValue = when (swipeToDismissBoxState.targetValue) {
                        SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.error.copy(alpha = 0.94f)
                        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error.copy(alpha = 0.94f)
                        SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                    }
                )

                val scale by animateFloatAsState(
                    label = "Icon scale animation",
                    targetValue = when (swipeToDismissBoxState.targetValue) {
                        SwipeToDismissBoxValue.StartToEnd -> 1f
                        SwipeToDismissBoxValue.EndToStart -> 1f
                        SwipeToDismissBoxValue.Settled -> 0.9f
                    }
                )

                val alignment = when (swipeToDismissBoxState.dismissDirection) {
                    SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                    SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                    SwipeToDismissBoxValue.Settled -> Alignment.Center
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = MaterialTheme.shapes.medium)
                        .background(color = backgroundColor)
                        .padding(all = 12.dp),
                    contentAlignment = alignment
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                    )
                }
            }
        ) {
            FavoriteItem(
                appWithColor = appWithColor,
                isDragging = isDragging
            )
        }
    }
}

@Composable
private fun ReorderableCollectionItemScope.FavoriteItem(
    appWithColor: AppWithColor,
    isDragging: Boolean,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        label = "Favorite item scale animation",
        targetValue = if (isDragging) 1.03f else 1f
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_drag_indicator),
                contentDescription = null,
                modifier = Modifier
                    .draggableHandle()
                    .padding(all = 12.dp)
            )
            Text(
                text = appWithColor.app.displayName,
                modifier = Modifier
                    .weight(weight = 1f)
                    .padding(vertical = 12.dp)
            )
        }
    }
}
