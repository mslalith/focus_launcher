package dev.mslalith.focuslauncher.core.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SettingsSelectableBottomContentItem(
    modifier: Modifier = Modifier,
    text: String,
    subText: String,
    trailing: @Composable () -> Unit = {},
    disabled: Boolean = false,
    height: Dp = 56.dp,
    horizontalPadding: Dp = 24.dp,
    content: @Composable () -> Unit
) {
    var showBottomContent by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = disabled) {
        if (disabled) {
            showBottomContent = false
        }
    }

    Column {
        SettingsSelectableItem(
            modifier = modifier,
            text = text,
            subText = subText,
            trailing = trailing,
            disabled = disabled,
            height = height,
            horizontalPadding = horizontalPadding,
            onClick = { showBottomContent = !showBottomContent }
        )
        AnimatedVisibility(visible = showBottomContent) {
            Box(
                modifier = Modifier.padding(horizontal = horizontalPadding)
            ) {
                content()
            }
        }
    }
}
