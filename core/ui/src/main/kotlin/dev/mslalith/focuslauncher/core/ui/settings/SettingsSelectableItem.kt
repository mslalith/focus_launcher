package dev.mslalith.focuslauncher.core.ui.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSelectableItem(
    modifier: Modifier = Modifier,
    text: String,
    subText: String? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    disabled: Boolean = false,
    backgroundColor: Color = Color.Transparent,
    onClick: (() -> Unit)?
) {
    val contentColor = MaterialTheme.colorScheme.onSurface
    val animatedContentColor by animateColorAsState(
        label = "Background color",
        targetValue = if (disabled) contentColor.copy(alpha = 0.38f) else contentColor
    )

    ListItem(
        modifier = modifier.clickable(enabled = !disabled && onClick != null) { onClick?.invoke() },
        colors = ListItemDefaults.colors(
            containerColor = backgroundColor,
            supportingColor = animatedContentColor,
            headlineColor = animatedContentColor
        ),
        headlineText = { Text(text = text) },
        supportingText = if (subText != null) {
            @Composable {
                Text(text = subText)
            }
        } else null,
        trailingContent = trailingContent
    )
}
