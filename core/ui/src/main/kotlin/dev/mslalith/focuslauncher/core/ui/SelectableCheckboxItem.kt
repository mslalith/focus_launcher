package dev.mslalith.focuslauncher.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SelectableCheckboxItem(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
    disabled: Boolean = false,
    onClick: () -> Unit
) {
    val onBackgroundColor = MaterialTheme.colorScheme.onSurface
    val disabledColor = onBackgroundColor.copy(alpha = 0.38f)

    ListItem(
        modifier = modifier.clickable { onClick() },
        colors = ListItemDefaults.colors(
            headlineColor = if (disabled) disabledColor else onBackgroundColor,
            leadingIconColor = if (disabled) disabledColor else onBackgroundColor
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
