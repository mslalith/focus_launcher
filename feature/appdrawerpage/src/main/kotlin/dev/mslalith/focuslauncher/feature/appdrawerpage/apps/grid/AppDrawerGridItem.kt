package dev.mslalith.focuslauncher.feature.appdrawerpage.apps.grid

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import dev.mslalith.focuslauncher.core.model.app.AppWithIconFavorite
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.APP_ICON_SIZE

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun AppDrawerGridItem(
    modifier: Modifier,
    appWithIconFavorite: AppWithIconFavorite,
    onClick: (AppWithIconFavorite) -> Unit,
    onLongClick: (AppWithIconFavorite) -> Unit
) {
    val iconBitmap = remember(key1 = appWithIconFavorite.appWithIcon.packageName) {
        appWithIconFavorite.appWithIcon.icon.toBitmap().asImageBitmap()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 4.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .combinedClickable(
                onClick = { onClick(appWithIconFavorite) },
                onLongClick = { onLongClick(appWithIconFavorite) }
            )
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(size = APP_ICON_SIZE * 1.5f)
        ) {
            Image(
                bitmap = iconBitmap,
                contentDescription = appWithIconFavorite.appWithIcon.displayName,
                modifier = Modifier.fillMaxSize()
            )
        }
        VerticalSpacer(spacing = 8.dp)
        Text(
            text = appWithIconFavorite.appWithIcon.displayName,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
