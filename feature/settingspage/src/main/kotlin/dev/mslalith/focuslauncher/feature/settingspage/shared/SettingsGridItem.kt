package dev.mslalith.focuslauncher.feature.settingspage.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun SettingsGridItem(
    modifier: Modifier = Modifier,
    text: String,
    showIcon: Boolean = false,
    horizontalPadding: Dp? = null,
    verticalPadding: Dp = 8.dp,
    icon: ImageVector? = null,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    val density = LocalDensity.current
    val usableHorizontalPadding = horizontalPadding ?: ITEM_PADDING

    val color = MaterialTheme.colors.onBackground
    var contentHeight by remember { mutableStateOf(0.dp) }
    val iconModifier = Modifier.size(contentHeight)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .clickable { onClick() }
            .padding(
                horizontal = usableHorizontalPadding,
                vertical = verticalPadding
            )
            .onSizeChanged {
                density.run { contentHeight = it.height.toDp() }
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showIcon) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .then(iconModifier)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = contentDescription ?: text,
                        tint = color
                    )
                }
            } else {
                Spacer(iconModifier)
            }
        }
        Text(
            text = text,
            style = TextStyle(color = color)
        )
        if (showIcon) {
            Spacer(iconModifier)
        }
    }
}
