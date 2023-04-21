package dev.mslalith.focuslauncher.feature.appdrawerpage.apps.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.APP_ICON_SIZE
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.ICON_INNER_HORIZONTAL_PADDING
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.ITEM_START_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CharacterHeader(
    character: Char,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        ),
        leadingContent = {
            Box(
                modifier = Modifier
                    .padding(start = ITEM_START_PADDING - ICON_INNER_HORIZONTAL_PADDING)
                    .size(size = APP_ICON_SIZE + (ICON_INNER_HORIZONTAL_PADDING * 2))
                    .clip(shape = MaterialTheme.shapes.small)
                    .background(color = backgroundColor)
                    .border(
                        width = 1.5f.dp,
                        color = contentColor.copy(alpha = 0.5f),
                        shape = MaterialTheme.shapes.small
                    )
            ) {
                Text(
                    text = "$character",
                    style = TextStyle(
                        color = contentColor,
                        fontSize = 18.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        headlineText = { Spacer(Modifier) }
    )
}
