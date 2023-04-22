package dev.mslalith.focuslauncher.feature.appdrawerpage.apps.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.graphics.drawable.toBitmap
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.APP_ICON_SIZE
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.ICON_INNER_HORIZONTAL_PADDING
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.ITEM_END_PADDING
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.ITEM_START_PADDING

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun AppDrawerListItem(
    app: AppWithIcon,
    showAppIcons: Boolean,
    onClick: (AppWithIcon) -> Unit,
    onLongClick: (AppWithIcon) -> Unit
) {
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
                onClick = { onClick(app) },
                onLongClick = { onLongClick(app) }
            ),
        leadingContent = if (showAppIcons) {
            @Composable {
                Image(
                    bitmap = app.icon.toBitmap().asImageBitmap(),
                    contentDescription = app.displayName,
                    modifier = Modifier
                        .padding(start = ITEM_START_PADDING)
                        .size(APP_ICON_SIZE)
                )
            }
        } else null,
        headlineText = {
            Text(
                text = app.displayName,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = textStartPadding)
            )
        }
    )
}
