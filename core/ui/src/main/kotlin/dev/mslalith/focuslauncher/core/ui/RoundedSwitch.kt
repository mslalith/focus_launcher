package dev.mslalith.focuslauncher.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun RoundedSwitch(
    checked: Boolean,
    enabled: Boolean = true
) {
    val thumbColor = MaterialTheme.colors.onBackground
    val trackColor = thumbColor.copy(alpha = 0.3f)
    val disabledThumbColor = thumbColor.copy(alpha = 0.2f)
    val disabledTrackColor = thumbColor.copy(alpha = 0.1f)

    val padding = 2.dp
    val thumbSize = 20.dp
    val trackWidth = (thumbSize * 1.75f) + (padding * 2)

    val horizontalBias by animateFloatAsState(targetValue = if (checked) 1f else -1f)
    val animatedThumbColor by animateColorAsState(targetValue = if (enabled) thumbColor else disabledThumbColor)
    val animatedTrackColor by animateColorAsState(targetValue = if (enabled) trackColor else disabledTrackColor)

    Box(
        modifier = Modifier
            .width(width = trackWidth)
            .clip(shape = CircleShape)
            .background(color = animatedTrackColor)
            .padding(all = padding)
            .heightIn(min = thumbSize),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .requiredSize(size = thumbSize)
                .clip(shape = CircleShape)
                .background(color = animatedThumbColor)
                .align(
                    alignment = BiasAlignment(
                        horizontalBias = horizontalBias,
                        verticalBias = 0f
                    )
                )
        )
    }
}
