package dev.mslalith.focuslauncher.feature.lunarcalendar.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableItem
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableSwitchItem
import dev.mslalith.focuslauncher.feature.lunarcalendar.LunarCalendarViewModel
import dev.mslalith.focuslauncher.feature.lunarcalendar.R
import dev.mslalith.focuslauncher.feature.lunarcalendar.model.LunarPhaseSettingsProperties

@Composable
fun LunarPhaseSettingsSheet(
    modifier: Modifier = Modifier,
    properties: LunarPhaseSettingsProperties
) {
    LunarPhaseSettingsSheetInternal(
        modifier = modifier,
        navigateToCurrentPlace = properties.navigateToCurrentPlace
    )
}

@Composable
internal fun LunarPhaseSettingsSheetInternal(
    modifier: Modifier = Modifier,
    lunarCalendarViewModel: LunarCalendarViewModel = hiltViewModel(),
    navigateToCurrentPlace: () -> Unit
) {
    val lunarCalendarState by lunarCalendarViewModel.lunarCalendarState.collectAsStateWithLifecycle()
    val currentPlace by lunarCalendarViewModel.currentPlaceStateFlow.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
    ) {
        PreviewLunarCalendar(showLunarPhase = lunarCalendarState.showLunarPhase)
        SettingsSelectableSwitchItem(
            text = stringResource(id = R.string.enable_lunar_phase),
            checked = lunarCalendarState.showLunarPhase,
            onClick = lunarCalendarViewModel::toggleShowLunarPhase
        )
        SettingsSelectableSwitchItem(
            text = stringResource(id = R.string.show_illumination_percent),
            checked = lunarCalendarState.showIlluminationPercent,
            disabled = !lunarCalendarState.showLunarPhase,
            onClick = lunarCalendarViewModel::toggleShowIlluminationPercent
        )
        SettingsSelectableSwitchItem(
            text = stringResource(id = R.string.show_upcoming_phase_details),
            checked = lunarCalendarState.showUpcomingPhaseDetails,
            disabled = !lunarCalendarState.showLunarPhase,
            onClick = lunarCalendarViewModel::toggleShowUpcomingPhaseDetails
        )
        SettingsSelectableItem(
            text = stringResource(id = R.string.current_place),
            subText = currentPlace.address ?: stringResource(id = R.string.not_available),
            disabled = !lunarCalendarState.showLunarPhase,
            onClick = navigateToCurrentPlace
        )
        VerticalSpacer(spacing = 12.dp)
    }
}
