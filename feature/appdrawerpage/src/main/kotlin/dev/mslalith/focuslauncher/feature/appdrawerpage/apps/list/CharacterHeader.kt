package dev.mslalith.focuslauncher.feature.appdrawerpage.apps.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.APP_ICON_SIZE
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.ICON_INNER_HORIZONTAL_PADDING
import dev.mslalith.focuslauncher.feature.appdrawerpage.utils.ITEM_START_PADDING

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun CharacterHeader(character: Char) {
    ListItem(
        icon = {
            Box(
                modifier = Modifier
                    .padding(start = ITEM_START_PADDING - ICON_INNER_HORIZONTAL_PADDING)
                    .size(size = APP_ICON_SIZE + (ICON_INNER_HORIZONTAL_PADDING * 2))
                    .clip(shape = MaterialTheme.shapes.small)
                    .background(color = MaterialTheme.colors.primaryVariant)
                    .border(
                        width = 1.5f.dp,
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                        shape = MaterialTheme.shapes.small
                    )
            ) {
                Text(
                    text = "$character",
                    style = TextStyle(
                        color = MaterialTheme.colors.onBackground,
                        fontSize = 18.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        text = { Spacer(Modifier) }
    )
}
