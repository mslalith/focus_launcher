package dev.mslalith.focuslauncher.ui.views.bottomsheets.settings

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.data.models.LunarPhaseSettingsProperties
import dev.mslalith.focuslauncher.extensions.VerticalSpacer
import dev.mslalith.focuslauncher.ui.viewmodels.SettingsViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.WidgetsViewModel
import dev.mslalith.focuslauncher.ui.views.SettingsSelectableSwitchItem
import dev.mslalith.focuslauncher.ui.views.widgets.LunarCalendar

@Composable
fun LunarPhaseSettingsSheet(
    properties: LunarPhaseSettingsProperties,
) {
    properties.apply {
        val showLunarPhase by settingsViewModel.showLunarPhaseStateFlow.collectAsState()
        val showIlluminationPercent by settingsViewModel.showIlluminationPercentStateFlow.collectAsState()
        val showUpcomingPhaseDetails by settingsViewModel.showUpcomingPhaseDetailsStateFlow.collectAsState()

        Column {
            PreviewLunarCalendar(
                settingsViewModel = settingsViewModel,
                widgetsViewModel = widgetsViewModel,
                showLunarPhase = showLunarPhase,
            )
            SettingsSelectableSwitchItem(
                text = "Enable Lunar Phase",
                checked = showLunarPhase,
                onClick = { settingsViewModel.toggleShowLunarPhase() }
            )
            SettingsSelectableSwitchItem(
                text = "Show Illumination Percent",
                checked = showIlluminationPercent,
                disabled = !showLunarPhase,
                onClick = { settingsViewModel.toggleShowIlluminationPercent() }
            )
            SettingsSelectableSwitchItem(
                text = "Show Upcoming Phase Details",
                checked = showUpcomingPhaseDetails,
                disabled = !showLunarPhase,
                onClick = { settingsViewModel.toggleShowUpcomingPhaseDetails() }
            )
            VerticalSpacer(spacing = bottomSpacing)
        }
    }
}

@Composable
private fun PreviewLunarCalendar(
    settingsViewModel: SettingsViewModel,
    widgetsViewModel: WidgetsViewModel,
    showLunarPhase: Boolean,
) {
    val height = 74.dp
    val horizontalPadding = 24.dp

    Crossfade(
        targetState = showLunarPhase,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .padding(top = 16.dp, bottom = 16.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colors.secondaryVariant),
    ) {
        if (it) {
            LunarCalendar(
                settingsViewModel = settingsViewModel,
                widgetsViewModel = widgetsViewModel,
                height = height,
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = height),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Enable Lunar Phase to preview",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = MaterialTheme.colors.onBackground,
                    ),
                )
            }
        }
    }
}
