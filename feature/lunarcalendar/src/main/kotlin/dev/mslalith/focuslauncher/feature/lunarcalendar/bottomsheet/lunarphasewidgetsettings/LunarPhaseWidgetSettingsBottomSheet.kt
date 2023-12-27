package dev.mslalith.focuslauncher.feature.lunarcalendar.bottomsheet.lunarphasewidgetsettings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.screens.CurrentPlaceScreen
import dev.mslalith.focuslauncher.core.screens.LunarPhaseWidgetSettingsBottomSheetScreen
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableItem
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableSwitchItem
import dev.mslalith.focuslauncher.feature.lunarcalendar.R
import dev.mslalith.focuslauncher.feature.lunarcalendar.settings.PreviewLunarCalendar

@CircuitInject(LunarPhaseWidgetSettingsBottomSheetScreen::class, SingletonComponent::class)
@Composable
fun LunarPhaseWidgetSettingsBottomSheet(
    state: LunarPhaseWidgetSettingsBottomSheetState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    LunarPhaseWidgetSettingsBottomSheet(
        modifier = modifier,
        state = state,
        onToggleShowLunarPhase = { eventSink(LunarPhaseWidgetSettingsBottomSheetUiEvent.ToggleShowLunarPhase) },
        onToggleShowIlluminationPercent = { eventSink(LunarPhaseWidgetSettingsBottomSheetUiEvent.ToggleShowIlluminationPercent) },
        onToggleShowUpcomingPhaseDetails = { eventSink(LunarPhaseWidgetSettingsBottomSheetUiEvent.ToggleShowUpcomingPhaseDetails) },
        navigateToCurrentPlace = { eventSink(LunarPhaseWidgetSettingsBottomSheetUiEvent.Goto(screen = CurrentPlaceScreen)) }
    )
}

@Composable
private fun LunarPhaseWidgetSettingsBottomSheet(
    state: LunarPhaseWidgetSettingsBottomSheetState,
    onToggleShowLunarPhase: () -> Unit,
    onToggleShowIlluminationPercent: () -> Unit,
    onToggleShowUpcomingPhaseDetails: () -> Unit,
    navigateToCurrentPlace: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        PreviewLunarCalendar(showLunarPhase = state.showLunarPhase)
        SettingsSelectableSwitchItem(
            text = stringResource(id = R.string.enable_lunar_phase),
            checked = state.showLunarPhase,
            onClick = onToggleShowLunarPhase
        )
        SettingsSelectableSwitchItem(
            text = stringResource(id = R.string.show_illumination_percent),
            checked = state.showIlluminationPercent,
            disabled = !state.showLunarPhase,
            onClick = onToggleShowIlluminationPercent
        )
        SettingsSelectableSwitchItem(
            text = stringResource(id = R.string.show_upcoming_phase_details),
            checked = state.showUpcomingPhaseDetails,
            disabled = !state.showLunarPhase,
            onClick = onToggleShowUpcomingPhaseDetails
        )
        SettingsSelectableItem(
            text = stringResource(id = R.string.current_place),
            subText = state.currentPlace.address ?: stringResource(id = R.string.not_available),
            disabled = !state.showLunarPhase,
            onClick = navigateToCurrentPlace
        )
        VerticalSpacer(spacing = 12.dp)
    }
}
