package dev.mslalith.focuslauncher.feature.appdrawerpage.apps.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.APP_ICON_SIZE
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.ICON_INNER_HORIZONTAL_PADDING
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.ITEM_END_PADDING
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.ITEM_START_PADDING

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun AppDrawerListItem(
    app: AppWithIcon,
    showAppIcons: Boolean,
    onClick: (AppWithIcon) -> Unit,
    onLongClick: (AppWithIcon) -> Unit
) {
    val image: @Composable () -> Unit = {
        Image(
            bitmap = app.icon.toBitmap().asImageBitmap(),
            contentDescription = app.displayName,
            modifier = Modifier
                .padding(start = ITEM_START_PADDING)
                .size(APP_ICON_SIZE)
        )
    }

    val textStartPadding = when (showAppIcons) {
        true -> ITEM_END_PADDING
        false -> ITEM_START_PADDING + ((APP_ICON_SIZE + ICON_INNER_HORIZONTAL_PADDING) / 4)
    }

    ListItem(
        modifier = Modifier
            .pointerInput(app.packageName) {
                detectTapGestures(
                    onTap = { onClick(app) },
                    onLongPress = { onLongClick(app) }
                )
            },
        icon = if (showAppIcons) image else null,
        text = {
            Text(
                text = app.displayName,
                style = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 18.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = textStartPadding)
            )
        }
    )
}
