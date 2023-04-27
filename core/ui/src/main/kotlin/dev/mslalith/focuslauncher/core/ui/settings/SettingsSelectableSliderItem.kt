package dev.mslalith.focuslauncher.core.ui.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
    horizontalPadding: Dp = 24.dp
) {
    SettingsSelectableBottomContentItem(
        modifier = modifier,
        text = text,
        subText = subText,
        disabled = disabled,
        horizontalPadding = horizontalPadding
    ) {
        val activeColor = MaterialTheme.colorScheme.primary
        val inactiveColor = MaterialTheme.colorScheme.onSurface
        var sliderValue by remember { mutableStateOf(value = value) }

        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            onValueChangeFinished = { onValueChangeFinished(sliderValue) },
            enabled = !disabled,
            valueRange = valueRange,
            steps = steps,
            colors = SliderDefaults.colors(
                thumbColor = activeColor,
                activeTrackColor = activeColor,
                inactiveTrackColor = inactiveColor.copy(alpha = 0.24f),
                activeTickColor = activeColor,
                inactiveTickColor = inactiveColor
            )
        )
    }
}
