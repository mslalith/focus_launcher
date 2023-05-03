package dev.mslalith.focuslauncher.feature.appdrawerpage.apps.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.graphics.drawable.toBitmap
import dev.mslalith.focuslauncher.core.model.app.AppWithIconFavorite
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.APP_ICON_SIZE
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.ICON_INNER_HORIZONTAL_PADDING
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.ITEM_END_PADDING
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.ITEM_START_PADDING

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun AppDrawerListItem(
    appWithIconFavorite: AppWithIconFavorite,
    showAppIcons: Boolean,
    onClick: (AppWithIconFavorite) -> Unit,
    onLongClick: (AppWithIconFavorite) -> Unit
) {
    val iconBitmap = remember(key1 = appWithIconFavorite.appWithIcon.packageName) {
        appWithIconFavorite.appWithIcon.icon.toBitmap().asImageBitmap()
    }

    val textStartPadding = when (showAppIcons) {
        true -> ITEM_END_PADDING
        false -> ITEM_START_PADDING + ((APP_ICON_SIZE + ICON_INNER_HORIZONTAL_PADDING) / 4)
    }

    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .combinedClickable(
                onClick = { onClick(appWithIconFavorite) },
                onLongClick = { onLongClick(appWithIconFavorite) }
            ),
        leadingContent = if (showAppIcons) {
            @Composable {
                Image(
                    bitmap = iconBitmap,
                    contentDescription = appWithIconFavorite.appWithIcon.displayName,
                    modifier = Modifier
                        .padding(start = ITEM_START_PADDING)
                        .size(size = APP_ICON_SIZE)
                )
            }
        } else null,
        headlineContent = {
            Text(
                text = appWithIconFavorite.appWithIcon.displayName,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = textStartPadding)
            )
        }
    )
}
