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
import dev.mslalith.focuslauncher.data.models.ClockAlignment
import dev.mslalith.focuslauncher.data.models.ClockSettingsProperties
import dev.mslalith.focuslauncher.extensions.verticalSpacer
import dev.mslalith.focuslauncher.ui.viewmodels.SettingsViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.WidgetsViewModel
import dev.mslalith.focuslauncher.ui.views.SettingsSelectableChooserItem
import dev.mslalith.focuslauncher.ui.views.SettingsSelectableSliderItem
import dev.mslalith.focuslauncher.ui.views.SettingsSelectableSwitchItem
import dev.mslalith.focuslauncher.ui.views.widgets.ClockWidget

@Composable
fun ClockSettingsSheet(
    properties: ClockSettingsProperties,
) {
    properties.apply {
        val showClock24 by settingsViewModel.showClock24StateFlow.collectAsState()
        val clockAlignment by settingsViewModel.clockAlignmentStateFlow.collectAsState()
        val clock24AnimationDuration by settingsViewModel.clock24AnimationDurationStateFlow.collectAsState()

        val textIconsList = listOf(
            ClockAlignment.START.text to R.drawable.ic_align_horizontal_left,
            ClockAlignment.CENTER.text to R.drawable.ic_align_horizontal_center,
            ClockAlignment.END.text to R.drawable.ic_align_horizontal_right,
        )

        Column {
            PreviewClock(
                widgetsViewModel = widgetsViewModel,
                settingsViewModel = settingsViewModel,
            )
            SettingsSelectableChooserItem(
                text = "Clock Position",
                subText = clockAlignment.text,
                textIconsList = textIconsList,
                selectedItem = clockAlignment.text,
                onItemSelected = { index ->
                    val alignmentName = textIconsList[index].first
                    val alignment = ClockAlignment.values().first { it.text == alignmentName }
                    settingsViewModel.updateClockAlignment(alignment)
                },
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
                onValueChangeFinished = { settingsViewModel.updateClock24AnimationDuration(it.toInt()) },
                valueRange = 300f..2400f,
                steps = 300,
            )
            bottomSpacing.verticalSpacer()
        }
    }
}

@Composable
private fun PreviewClock(
    settingsViewModel: SettingsViewModel,
    widgetsViewModel: WidgetsViewModel,
) {
    val height = 134.dp
    val horizontalPadding = 24.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = height)
            .padding(horizontal = horizontalPadding)
            .padding(top = 16.dp, bottom = 16.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colors.secondaryVariant),
    ) {
        ClockWidget(
            settingsViewModel = settingsViewModel,
            widgetsViewModel = widgetsViewModel,
            horizontalPadding = 22.dp,
            centerVertically = true,
        )
    }
}
