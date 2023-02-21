package dev.mslalith.focuslauncher.core.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.ui.RoundedSwitch

@Composable
fun SettingsSelectableSwitchItem(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
    disabled: Boolean = false,
    height: Dp = 48.dp,
    horizontalPadding: Dp = 24.dp,
    onClick: () -> Unit
) {
    SettingsSelectableItem(
        modifier = modifier,
        text = text,
        disabled = disabled,
        height = height,
        horizontalPadding = horizontalPadding,
        onClick = onClick,
        trailing = {
            RoundedSwitch(
                checked = checked,
                enabled = !disabled
            )
        }
    )
}
