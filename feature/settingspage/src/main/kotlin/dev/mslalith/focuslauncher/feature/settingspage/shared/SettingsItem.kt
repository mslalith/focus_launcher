package dev.mslalith.focuslauncher.feature.settingspage.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal val ITEM_PADDING = 28.dp

@Composable
internal fun SettingsItem(
    modifier: Modifier = Modifier,
    text: String,
    horizontalPadding: Dp? = null,
    verticalPadding: Dp = 12.dp,
    afterLeadingPadding: Dp = 20.dp,
    leading: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val usableHorizontalPadding = (horizontalPadding ?: ITEM_PADDING) * .75f

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = usableHorizontalPadding)
            .clip(shape = MaterialTheme.shapes.small)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(
                horizontal = usableHorizontalPadding,
                vertical = verticalPadding
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(visible = leading != null) {
            Box(
                modifier = Modifier.padding(end = afterLeadingPadding)
            ) {
                leading?.invoke()
            }
        }
        Crossfade(
            label = "Cross Fade Settings Item Text",
            targetState = text,
        ) {
            Text(text = it)
        }
    }
}
