package dev.mslalith.focuslauncher.feature.appdrawerpage.apps.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerIconViewType
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import dev.mslalith.focuslauncher.core.ui.remember.rememberAppColor
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.Constants.APP_ICON_SIZE
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.Constants.ICON_INNER_HORIZONTAL_PADDING
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.Constants.ITEM_END_PADDING
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.Constants.ITEM_START_PADDING

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun AppDrawerListItem(
    appDrawerItem: AppDrawerItem,
    appDrawerIconViewType: AppDrawerIconViewType,
    onClick: (AppDrawerItem) -> Unit,
    onLongClick: (AppDrawerItem) -> Unit
) {
    val iconBitmap = remember(key1 = appDrawerItem.app.packageName) {
        appDrawerItem.icon.toBitmap().asImageBitmap()
    }

    val textStartPadding = when (appDrawerIconViewType) {
        AppDrawerIconViewType.TEXT -> ITEM_START_PADDING + ((APP_ICON_SIZE + ICON_INNER_HORIZONTAL_PADDING) / 4)
        AppDrawerIconViewType.ICONS, AppDrawerIconViewType.COLORED -> ITEM_END_PADDING
    }

    fun iconViewContent(): @Composable (() -> Unit)? {
        return when (appDrawerIconViewType) {
            AppDrawerIconViewType.TEXT -> null
            AppDrawerIconViewType.ICONS -> {
                @Composable {
                    Image(
                        bitmap = iconBitmap,
                        contentDescription = appDrawerItem.app.displayName,
                        modifier = Modifier
                            .padding(start = ITEM_START_PADDING)
                            .size(size = APP_ICON_SIZE)
                    )
                }
            }
            AppDrawerIconViewType.COLORED -> {
                @Composable {
                    val appIconBasedColor = rememberAppColor(graphicsColor = appDrawerItem.color)

                    Box(
                        modifier = Modifier
                            .padding(start = ITEM_START_PADDING)
                            .size(size = APP_ICON_SIZE)
                            .background(
                                color = appIconBasedColor,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }

    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .combinedClickable(
                onClick = { onClick(appDrawerItem) },
                onLongClick = { onLongClick(appDrawerItem) }
            ),
        leadingContent = iconViewContent(),
        headlineContent = {
            Text(
                text = appDrawerItem.app.displayName,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = textStartPadding)
            )
        }
    )
}
