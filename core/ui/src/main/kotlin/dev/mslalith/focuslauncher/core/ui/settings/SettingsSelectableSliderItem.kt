package dev.mslalith.focuslauncher.core.ui.settings

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SettingsSelectableSliderItem(
    modifier: Modifier = Modifier,
    text: String,
    subText: String,
    value: Float,
    onValueChangeFinished: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    disabled: Boolean = false,
    height: Dp = 56.dp,
    horizontalPadding: Dp = 24.dp
) {
    val thumbColor = MaterialTheme.colors.onBackground
    val inactiveColor = thumbColor.copy(alpha = 0.1f)
    var sliderValue by remember { mutableStateOf(value) }

    SettingsSelectableBottomContentItem(
        modifier = modifier,
        text = text,
        subText = subText,
        disabled = disabled,
        height = height,
        horizontalPadding = horizontalPadding
    ) {
        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            onValueChangeFinished = { onValueChangeFinished(sliderValue) },
            enabled = !disabled,
            valueRange = valueRange,
            steps = steps,
            colors = SliderDefaults.colors(
                thumbColor = thumbColor,
                activeTrackColor = thumbColor,
                inactiveTrackColor = inactiveColor,
                activeTickColor = thumbColor,
                inactiveTickColor = inactiveColor
            )
        )
    }
}
