package dev.mslalith.focuslauncher.feature.appdrawerpage.apps.grid

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.extensions.toAppWithIconList
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun AppsGrid(
    apps: List<App>,
    onAppClick: (AppWithIcon) -> Unit,
    onAppLongClick: (AppWithIcon) -> Unit
) {
    val columnCount = 4
    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val topSpacing = configuration.screenHeightDp.dp * 0.2f
    val bottomSpacing = configuration.screenHeightDp.dp * 0.05f

    val appsList = remember(key1 = apps) {
        apps.toAppWithIconList(context)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = columnCount),
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        repeat(columnCount) {
            item { VerticalSpacer(spacing = topSpacing) }
        }

        items(
            items = appsList,
            key = { it.packageName }
        ) { app ->
            AppDrawerGridItem(
                app = app,
                onClick = onAppClick,
                onLongClick = onAppLongClick,
                modifier = Modifier.animateItemPlacement()
            )
        }

        repeat(columnCount) {
            item { VerticalSpacer(spacing = bottomSpacing) }
        }
    }
}
