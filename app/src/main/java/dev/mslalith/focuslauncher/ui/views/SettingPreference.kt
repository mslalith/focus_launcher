package dev.mslalith.focuslauncher.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
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
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val SELECTABLE_ITEM_HEIGHT = 48.dp
private val SELECTABLE_BOTTOM_CONTENT_ITEM_HEIGHT = 56.dp
private val SELECTABLE_ITEM_HORIZONTAL_PADDING = 24.dp

@Composable
fun SettingsSelectableChooserItem(
    modifier: Modifier = Modifier,
    text: String,
    subText: String,
    textIconsList: List<Pair<String, Int>>,
    selectedItem: String,
    onItemSelected: (Int) -> Unit,
    disabled: Boolean = false,
    height: Dp = SELECTABLE_BOTTOM_CONTENT_ITEM_HEIGHT,
    horizontalPadding: Dp = 24.dp
) {
    SettingsSelectableBottomContentItem(
        modifier = modifier,
        text = text,
        subText = subText,
        disabled = disabled,
        height = height,
        horizontalPadding = horizontalPadding
    ) {
        ChooserGroup(
            textIconsList = textIconsList,
            onItemSelected = onItemSelected,
            selectedItem = selectedItem,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

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
    height: Dp = SELECTABLE_BOTTOM_CONTENT_ITEM_HEIGHT,
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

@Composable
fun SettingsSelectableSwitchItem(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
    disabled: Boolean = false,
    height: Dp = SELECTABLE_ITEM_HEIGHT,
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

@Composable
fun SettingsSelectableBottomContentItem(
    modifier: Modifier = Modifier,
    text: String,
    subText: String,
    trailing: @Composable () -> Unit = {},
    disabled: Boolean = false,
    height: Dp = SELECTABLE_BOTTOM_CONTENT_ITEM_HEIGHT,
    horizontalPadding: Dp = SELECTABLE_ITEM_HORIZONTAL_PADDING,
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

@Composable
fun SettingsSelectableItem(
    modifier: Modifier = Modifier,
    text: String,
    subText: String? = null,
    trailing: @Composable () -> Unit = {},
    disabled: Boolean = false,
    height: Dp = SELECTABLE_ITEM_HEIGHT,
    horizontalPadding: Dp = SELECTABLE_ITEM_HORIZONTAL_PADDING,
    onClick: (() -> Unit)?
) {
    val onBackgroundColor = MaterialTheme.colors.onBackground
    val textColor by animateColorAsState(
        targetValue = if (disabled) onBackgroundColor.copy(alpha = 0.4f) else onBackgroundColor
    )
    val subTextColor by animateColorAsState(
        targetValue = if (disabled) onBackgroundColor.copy(alpha = 0.4f) else onBackgroundColor.copy(alpha = 0.6f)
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
                Text(
                    text = text,
                    style = TextStyle(
                        color = textColor,
                        fontSize = 16.sp
                    )
                )
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
        if (subText != null) {
            Crossfade(
                targetState = subText,
                modifier = Modifier
                    .weight(weight = 1f)
                    .padding(horizontal = horizontalPadding)
            ) {
                Text(
                    text = it,
                    style = TextStyle(
                        color = subTextColor,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}
