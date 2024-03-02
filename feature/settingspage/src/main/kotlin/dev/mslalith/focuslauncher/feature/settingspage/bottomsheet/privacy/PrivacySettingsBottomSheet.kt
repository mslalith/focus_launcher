package dev.mslalith.focuslauncher.feature.settingspage.bottomsheet.privacy

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.screens.PrivacySettingsBottomSheetScreen
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableSwitchItem
import dev.mslalith.focuslauncher.feature.settingspage.R

@CircuitInject(PrivacySettingsBottomSheetScreen::class, SingletonComponent::class)
@Composable
fun PrivacySettingsBottomSheet(
    state: PrivacySettingsBottomSheetState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    Column(
        modifier = modifier
    ) {
        SettingsSelectableSwitchItem(
            text = stringResource(id = R.string.report_app_crashes),
            checked = state.reportCrashes,
            onClick = { eventSink(PrivacySettingsBottomSheetUiEvent.ToggleReportCrashes) }
        )
    }
}
