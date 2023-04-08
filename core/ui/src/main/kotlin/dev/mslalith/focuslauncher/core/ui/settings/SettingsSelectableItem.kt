package dev.mslalith.focuslauncher.core.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsSelectableItem(
    modifier: Modifier = Modifier,
    text: String,
    subText: String? = null,
    trailing: @Composable () -> Unit = {},
    disabled: Boolean = false,
    height: Dp = 48.dp,
    horizontalPadding: Dp = 24.dp,
    onClick: (() -> Unit)?
) {
    val onBackgroundColor = MaterialTheme.colors.onBackground
    val textColor by animateColorAsState(
        targetValue = if (disabled) onBackgroundColor.copy(alpha = 0.4f) else onBackgroundColor
    )
    val subTextColor by animateColorAsState(
        targetValue = if (disabled) onBackgroundColor.copy(alpha = 0.4f) else onBackgroundColor.copy(
            alpha = 0.6f
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(height = height)
            .clickable(enabled = !disabled && onClick != null) { onClick?.invoke() }
    ) {
        Row(
            modifier = Modifier.weight(weight = 1f)
        ) {
            Box(
                modifier = Modifier
                    .weight(weight = 1f)
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = horizontalPadding)
            ) {
                Column {
                    Text(
                        text = text,
                        style = TextStyle(
                            color = textColor,
                            fontSize = 16.sp
                        )
                    )
                    AnimatedVisibility(
                        visible = subText != null
                    ) {
                        Text(
                            text = subText ?: "",
                            style = TextStyle(
                                color = subTextColor,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = horizontalPadding),
                contentAlignment = Alignment.Center
            ) {
                trailing()
            }
        }
    }
}
