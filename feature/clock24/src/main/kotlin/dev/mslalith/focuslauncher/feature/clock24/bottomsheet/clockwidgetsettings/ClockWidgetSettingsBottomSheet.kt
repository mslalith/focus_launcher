package dev.mslalith.focuslauncher.feature.clock24.bottomsheet.clockwidgetsettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import dev.mslalith.focuslauncher.core.model.Constants
import dev.mslalith.focuslauncher.core.screens.ClockWidgetSettingsBottomSheetScreen
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.extensions.string
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableChooserItem
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableSliderItem
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableSwitchItem
import dev.mslalith.focuslauncher.feature.clock24.R
import dev.mslalith.focuslauncher.feature.clock24.settings.PreviewClock
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.roundToInt

@CircuitInject(ClockWidgetSettingsBottomSheetScreen::class, SingletonComponent::class)
@Composable
fun ClockWidgetSettingsBottomSheet(
    state: ClockWidgetSettingsBottomSheetState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    ClockWidgetSettingsBottomSheet(
        modifier = modifier,
        state = state,
        toggleClock24 = { eventSink(ClockWidgetSettingsBottomSheetUiEvent.ToggleClock24) },
        toggleUse24Hour = { eventSink(ClockWidgetSettingsBottomSheetUiEvent.ToggleUse24Hour) },
        onUpdateClockAlignment = { eventSink(ClockWidgetSettingsBottomSheetUiEvent.UpdateClockAlignment(clockAlignment = it)) },
        onUpdateClock24AnimationDuration = { eventSink(ClockWidgetSettingsBottomSheetUiEvent.UpdateClock24AnimationDuration(duration = it)) }
    )
}

@Composable
private fun ClockWidgetSettingsBottomSheet(
    state: ClockWidgetSettingsBottomSheetState,
    toggleClock24: () -> Unit,
    toggleUse24Hour: () -> Unit,
    onUpdateClockAlignment: (ClockAlignment) -> Unit,
    onUpdateClock24AnimationDuration: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
            subText = state.clockAlignment.uiText.string(),
            textIconsList = textIconsList,
            selectedItem = state.clockAlignment.uiText.string(),
            showText = false,
            itemHorizontalArrangement = Arrangement.Center,
            onItemSelected = { index ->
                val alignmentName = textIconsList[index].first
                val alignment = ClockAlignment.values().first { it.uiText.string(context = context) == alignmentName }
                onUpdateClockAlignment(alignment)
            }
        )
        SettingsSelectableSwitchItem(
            text = stringResource(id = R.string.enable_clock_24),
            checked = state.showClock24,
            onClick = toggleClock24
        )
        SettingsSelectableSwitchItem(
            text = stringResource(id = R.string.use_24_hour),
            checked = state.use24Hour,
            onClick = toggleUse24Hour
        )
        SettingsSelectableSliderItem(
            text = stringResource(id = R.string.animation_duration),
            subText = "${state.clock24AnimationDuration}ms",
            disabled = !state.showClock24,
            value = state.clock24AnimationDuration.toFloat(),
            onValueChangeFinished = { onUpdateClock24AnimationDuration(it.roundToInt()) },
            valueRange = Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_DURATION_RANGE,
            steps = Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_STEP
        )
        VerticalSpacer(spacing = 12.dp)
    }
}
