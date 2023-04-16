package dev.mslalith.focuslauncher.feature.appdrawerpage.apps.grid

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreviewAppsGrid(
    modifier: Modifier = Modifier,
    apps: List<AppWithIcon>,
) {
    val columnCount = 4

    val configuration = LocalConfiguration.current
    val bottomSpacing = configuration.screenHeightDp.dp * 0.05f

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = columnCount),
        modifier = modifier.padding(horizontal = 24.dp)
    ) {
        items(
            items = apps,
            key = { it.packageName.hashCode() + (31 * it.icon.hashCode()) }
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
