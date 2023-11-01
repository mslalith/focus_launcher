package dev.mslalith.focuslauncher.feature.clock24.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_DURATION_RANGE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_STEP
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.extensions.string
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableChooserItem
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableSliderItem
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableSwitchItem
import dev.mslalith.focuslauncher.feature.clock24.Clock24ViewModel
import dev.mslalith.focuslauncher.feature.clock24.R
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.roundToInt

@Composable
fun ClockSettingsSheet(
    modifier: Modifier = Modifier
) {
    ClockSettingsSheetInternal(modifier = modifier)
}

@Composable
internal fun ClockSettingsSheetInternal(
    modifier: Modifier = Modifier,
    clock24ViewModel: Clock24ViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val clock24State by clock24ViewModel.clock24State.collectAsStateWithLifecycle()

    val textIconsList = remember {
        listOf(
            ClockAlignment.START.uiText to R.drawable.ic_align_horizontal_left,
            ClockAlignment.CENTER.uiText to R.drawable.ic_align_horizontal_center,
            ClockAlignment.END.uiText to R.drawable.ic_align_horizontal_right
        ).map { it.first.string(context = context) to it.second }.toImmutableList()
    }

    Column(
        modifier = modifier
    ) {
        PreviewClock()
        SettingsSelectableChooserItem(
            text = stringResource(id = R.string.clock_position),
            subText = clock24State.clockAlignment.uiText.string(),
            textIconsList = textIconsList,
            selectedItem = clock24State.clockAlignment.uiText.string(),
            showText = false,
            itemHorizontalArrangement = Arrangement.Center,
            onItemSelected = { index ->
                val alignmentName = textIconsList[index].first
                val alignment = ClockAlignment.values().first { it.uiText.string(context = context) == alignmentName }
                clock24ViewModel.updateClockAlignment(clockAlignment = alignment)
            }
        )
        SettingsSelectableSwitchItem(
            text = stringResource(id = R.string.enable_clock_24),
            checked = clock24State.showClock24,
            onClick = clock24ViewModel::toggleClock24
        )
        SettingsSelectableSwitchItem(
            text = stringResource(id = R.string.use_24_hour),
            checked = clock24State.use24Hour,
            onClick = clock24ViewModel::toggleUse24Hour
        )
        SettingsSelectableSliderItem(
            text = stringResource(id = R.string.animation_duration),
            subText = "${clock24State.clock24AnimationDuration}ms",
            disabled = !clock24State.showClock24,
            value = clock24State.clock24AnimationDuration.toFloat(),
            onValueChangeFinished = { clock24ViewModel.updateClock24AnimationDuration(duration = it.roundToInt()) },
            valueRange = DEFAULT_CLOCK_24_ANIMATION_DURATION_RANGE,
            steps = DEFAULT_CLOCK_24_ANIMATION_STEP
        )
        VerticalSpacer(spacing = 12.dp)
    }
}
