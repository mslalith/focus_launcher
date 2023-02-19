package dev.mslalith.focuslauncher.ui.views.bottomsheets.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.R
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import dev.mslalith.focuslauncher.data.models.ClockSettingsProperties
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_DURATION_RANGE
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_STEP
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.feature.clock24.ClockWidget
import dev.mslalith.focuslauncher.ui.views.SettingsSelectableChooserItem
import dev.mslalith.focuslauncher.ui.views.SettingsSelectableSliderItem
import dev.mslalith.focuslauncher.ui.views.SettingsSelectableSwitchItem
import kotlin.math.roundToInt

@Composable
fun ClockSettingsSheet(
    properties: ClockSettingsProperties
) {
    properties.apply {
        val showClock24 by settingsViewModel.showClock24StateFlow.collectAsState()
        val clockAlignment by settingsViewModel.clockAlignmentStateFlow.collectAsState()
        val clock24AnimationDuration by settingsViewModel.clock24AnimationDurationStateFlow.collectAsState()

        val textIconsList = listOf(
            ClockAlignment.START.text to R.drawable.ic_align_horizontal_left,
            ClockAlignment.CENTER.text to R.drawable.ic_align_horizontal_center,
            ClockAlignment.END.text to R.drawable.ic_align_horizontal_right
        )

        Column {
            PreviewClock()
            SettingsSelectableChooserItem(
                text = "Clock Position",
                subText = clockAlignment.text,
                textIconsList = textIconsList,
                selectedItem = clockAlignment.text,
                onItemSelected = { index ->
                    val alignmentName = textIconsList[index].first
                    val alignment = ClockAlignment.values().first { it.text == alignmentName }
                    settingsViewModel.updateClockAlignment(alignment)
                }
            )
            SettingsSelectableSwitchItem(
                text = "Enable Clock 24",
                checked = showClock24,
                onClick = { settingsViewModel.toggleClock24() }
            )
            SettingsSelectableSliderItem(
                text = "Animation Duration",
                subText = "${clock24AnimationDuration}ms",
                disabled = !showClock24,
                value = clock24AnimationDuration.toFloat(),
                onValueChangeFinished = { settingsViewModel.updateClock24AnimationDuration(it.roundToInt()) },
                valueRange = DEFAULT_CLOCK_24_ANIMATION_DURATION_RANGE,
                steps = DEFAULT_CLOCK_24_ANIMATION_STEP
            )
            VerticalSpacer(spacing = bottomSpacing)
        }
    }
}

@Composable
private fun PreviewClock() {
    val height = 134.dp
    val horizontalPadding = 24.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = height)
            .padding(horizontal = horizontalPadding)
            .padding(top = 16.dp, bottom = 16.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colors.secondaryVariant)
    ) {
        ClockWidget(
            horizontalPadding = 22.dp,
            centerVertically = true
        )
    }
}
