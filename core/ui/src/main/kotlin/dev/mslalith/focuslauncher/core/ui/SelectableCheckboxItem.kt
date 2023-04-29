package dev.mslalith.focuslauncher.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SelectableCheckboxItem(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
    disabled: Boolean = false,
    containerColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    val disabledColor = contentColor.copy(alpha = 0.38f)

    ListItem(
        modifier = modifier.clickable { onClick() },
        colors = ListItemDefaults.colors(
            containerColor = containerColor,
            headlineColor = if (disabled) disabledColor else contentColor,
            leadingIconColor = if (disabled) disabledColor else contentColor
        ),
        headlineContent = {
            Text(text = text)
        },
        leadingContent = {
            Checkbox(
                checked = checked,
                enabled = !disabled,
                onCheckedChange = null
            )
        }
    )
}
