package dev.mslalith.focuslauncher.feature.appdrawerpage.apps.grid

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.common.LoadingState
import dev.mslalith.focuslauncher.core.ui.DotWaveLoader
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon

@Composable
fun PreviewAppsGrid(
    modifier: Modifier = Modifier,
    appsState: LoadingState<List<AppWithIcon>>,
) {
    Crossfade(
        label = "Apps Grid Cross Fade",
        targetState = appsState,
        modifier = modifier.padding(horizontal = 24.dp)
    ) { appsLoadingState ->
        when (appsLoadingState) {
            is LoadingState.Loaded -> {
                PreviewAppsContent(apps = appsLoadingState.value)
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
    apps: List<AppWithIcon>
) {
    val columnCount = 4

    val configuration = LocalConfiguration.current
    val bottomSpacing = configuration.screenHeightDp.dp * 0.05f

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = columnCount),
        modifier = modifier
    ) {
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

        repeat(columnCount) {
            item { VerticalSpacer(spacing = bottomSpacing) }
        }
    }
}
