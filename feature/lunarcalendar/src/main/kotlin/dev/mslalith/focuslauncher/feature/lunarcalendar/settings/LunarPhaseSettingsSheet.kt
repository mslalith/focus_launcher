package dev.mslalith.focuslauncher.feature.lunarcalendar.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableItem
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableSwitchItem
import dev.mslalith.focuslauncher.feature.lunarcalendar.LunarCalendarViewModel
import dev.mslalith.focuslauncher.feature.lunarcalendar.model.LunarPhaseSettingsProperties

@Composable
fun LunarPhaseSettingsSheet(
    properties: LunarPhaseSettingsProperties
) {
    LunarPhaseSettingsSheet(
        lunarCalendarViewModel = hiltViewModel(),
        navigateToCurrentPlace = properties.navigateToCurrentPlace
    )
}

@Composable
internal fun LunarPhaseSettingsSheet(
    lunarCalendarViewModel: LunarCalendarViewModel,
    navigateToCurrentPlace: () -> Unit
) {
    val lunarCalendarState by lunarCalendarViewModel.lunarCalendarState.collectAsStateWithLifecycle()
    val currentPlace by lunarCalendarViewModel.currentPlaceStateFlow.collectAsStateWithLifecycle()

    Column {
        PreviewLunarCalendar(showLunarPhase = lunarCalendarState.showLunarPhase)
        SettingsSelectableSwitchItem(
            text = "Enable Lunar Phase",
            checked = lunarCalendarState.showLunarPhase,
            onClick = lunarCalendarViewModel::toggleShowLunarPhase
        )
        SettingsSelectableSwitchItem(
            text = "Show Illumination Percent",
            checked = lunarCalendarState.showIlluminationPercent,
            disabled = !lunarCalendarState.showLunarPhase,
            onClick = lunarCalendarViewModel::toggleShowIlluminationPercent
        )
        SettingsSelectableSwitchItem(
            text = "Show Upcoming Phase Details",
            checked = lunarCalendarState.showUpcomingPhaseDetails,
            disabled = !lunarCalendarState.showLunarPhase,
            onClick = lunarCalendarViewModel::toggleShowUpcomingPhaseDetails
        )
        SettingsSelectableItem(
            text = "Current Place",
            subText = currentPlace.address,
            disabled = !lunarCalendarState.showLunarPhase,
            onClick = navigateToCurrentPlace
        )
        VerticalSpacer(spacing = 12.dp)
    }
}
