package dev.mslalith.focuslauncher.feature.appdrawerpage.apps.grid

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.common.LoadingState
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon
import dev.mslalith.focuslauncher.core.ui.DotWaveLoader
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.modifiers.verticalFadeOutEdge

@Composable
fun PreviewAppsGrid(
    modifier: Modifier = Modifier,
    appsState: LoadingState<List<AppWithIcon>>,
    topSpacing: Dp = 16.dp,
    bottomSpacing: Dp = 16.dp
) {
    Crossfade(
        label = "Apps Grid Cross Fade",
        targetState = appsState,
        modifier = modifier
    ) { appsLoadingState ->
        when (appsLoadingState) {
            is LoadingState.Loaded -> {
                PreviewAppsContent(
                    apps = appsLoadingState.value,
                    topSpacing = topSpacing,
                    bottomSpacing = bottomSpacing
                )
            }
            LoadingState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    DotWaveLoader()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PreviewAppsContent(
    modifier: Modifier = Modifier,
    apps: List<AppWithIcon>,
    columnCount: Int = 4,
    topSpacing: Dp,
    bottomSpacing: Dp
) {

    Box(
        modifier = modifier
            .verticalFadeOutEdge(
                height = 16.dp,
                color = MaterialTheme.colorScheme.surface
            )
            .padding(horizontal = 24.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = columnCount)
        ) {
            repeat(times = columnCount) {
                item { VerticalSpacer(spacing = topSpacing) }
            }

            items(
                items = apps,
                key = { it.uniqueKey }
            ) { app ->
                AppDrawerGridItem(
                    app = app,
                    onClick = {},
                    onLongClick = {},
                    modifier = Modifier.animateItemPlacement()
                )
            }

            repeat(times = columnCount) {
                item { VerticalSpacer(spacing = bottomSpacing) }
            }
        }
    }
}
