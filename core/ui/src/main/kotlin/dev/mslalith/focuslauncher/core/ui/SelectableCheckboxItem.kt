package dev.mslalith.focuslauncher.core.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SelectableCheckboxItem(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
    disabled: Boolean = false,
    height: Dp = 48.dp,
    iconWidth: Dp = 56.dp,
    onClick: () -> Unit
) {
    val backgroundColor = MaterialTheme.colors.background
    val onBackgroundColor = MaterialTheme.colors.onBackground
    val disabledColor = onBackgroundColor.copy(alpha = 0.4f)

    SelectableItem(
        modifier = modifier,
        text = text,
        height = height,
        iconWidth = iconWidth,
        onClick = onClick,
        leading = {
            Checkbox(
                checked = checked,
                onCheckedChange = null,
                colors = CheckboxDefaults.colors(
                    checkedColor = if (disabled) disabledColor else onBackgroundColor,
                    uncheckedColor = if (disabled) disabledColor else onBackgroundColor,
                    checkmarkColor = backgroundColor
                ),
                modifier = Modifier
                    .padding(12.dp)
                    .padding(start = 8.dp, end = 8.dp)
            )
        }
    )
}
