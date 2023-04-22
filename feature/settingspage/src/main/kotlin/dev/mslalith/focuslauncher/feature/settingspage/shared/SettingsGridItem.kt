package dev.mslalith.focuslauncher.feature.settingspage.shared

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.ui.HorizontalSpacer

@Composable
internal fun SettingsGridItem(
    modifier: Modifier = Modifier,
    text: String,
    showIcon: Boolean = false,
    verticalPadding: Dp = 8.dp,
    @DrawableRes iconRes: Int? = null,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    val density = LocalDensity.current

    var contentHeight by remember { mutableStateOf(0.dp) }
    val iconModifier = Modifier.size(contentHeight)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .clickable { onClick() }
            .padding(vertical = verticalPadding)
            .onSizeChanged {
                density.run { contentHeight = it.height.toDp() }
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showIcon) {
            if (iconRes != null) {
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .then(iconModifier)
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = contentDescription ?: text,
                    )
                }
            } else {
                HorizontalSpacer(spacing = contentHeight)
            }
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
        if (showIcon) HorizontalSpacer(spacing = contentHeight)
    }
}
