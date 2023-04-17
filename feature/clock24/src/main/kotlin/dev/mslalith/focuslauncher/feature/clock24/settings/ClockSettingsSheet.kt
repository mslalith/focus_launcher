package dev.mslalith.focuslauncher.feature.clock24.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_DURATION_RANGE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_STEP
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableChooserItem
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableSliderItem
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableSwitchItem
import dev.mslalith.focuslauncher.feature.clock24.Clock24ViewModel
import dev.mslalith.focuslauncher.feature.clock24.R
import kotlin.math.roundToInt

@Composable
fun ClockSettingsSheet() {
    ClockSettingsSheet(
        clock24ViewModel = hiltViewModel()
    )
}

@Composable
internal fun ClockSettingsSheet(
    clock24ViewModel: Clock24ViewModel,
) {
    val clock24State by clock24ViewModel.clock24State.collectAsStateWithLifecycle()

    val textIconsList = remember {
        listOf(
            ClockAlignment.START.text to R.drawable.ic_align_horizontal_left,
            ClockAlignment.CENTER.text to R.drawable.ic_align_horizontal_center,
            ClockAlignment.END.text to R.drawable.ic_align_horizontal_right
        )
    }

    Column {
        PreviewClock()
        SettingsSelectableChooserItem(
            text = "Clock Position",
            subText = clock24State.clockAlignment.text,
            textIconsList = textIconsList,
            selectedItem = clock24State.clockAlignment.text,
            onItemSelected = { index ->
                val alignmentName = textIconsList[index].first
                val alignment = ClockAlignment.values().first { it.text == alignmentName }
                clock24ViewModel.updateClockAlignment(alignment)
            }
        )
        SettingsSelectableSwitchItem(
            text = "Enable Clock 24",
            checked = clock24State.showClock24,
            onClick = clock24ViewModel::toggleClock24
        )
        SettingsSelectableSliderItem(
            text = "Animation Duration",
            subText = "${clock24State.clock24AnimationDuration}ms",
            disabled = !clock24State.showClock24,
            value = clock24State.clock24AnimationDuration.toFloat(),
            onValueChangeFinished = { clock24ViewModel.updateClock24AnimationDuration(it.roundToInt()) },
            valueRange = DEFAULT_CLOCK_24_ANIMATION_DURATION_RANGE,
            steps = DEFAULT_CLOCK_24_ANIMATION_STEP
        )
        VerticalSpacer(spacing = 12.dp)
    }
}
