package dev.mslalith.focuslauncher.ui.views.bottomsheets.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dev.mslalith.focuslauncher.R
import dev.mslalith.focuslauncher.data.models.LunarPhaseSettingsProperties
import dev.mslalith.focuslauncher.extensions.HorizontalSpacer
import dev.mslalith.focuslauncher.extensions.VerticalSpacer
import dev.mslalith.focuslauncher.ui.viewmodels.SettingsViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.WidgetsViewModel
import dev.mslalith.focuslauncher.ui.views.SettingsSelectableSwitchItem
import dev.mslalith.focuslauncher.ui.views.onLifecycleEventChange
import dev.mslalith.focuslauncher.ui.views.widgets.LunarCalendar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LunarPhaseSettingsSheet(
    properties: LunarPhaseSettingsProperties,
) {
    val horizontalPadding = 24.dp
    properties.apply {
        val showLunarPhase by settingsViewModel.showLunarPhaseStateFlow.collectAsState()
        val showIlluminationPercent by settingsViewModel.showIlluminationPercentStateFlow.collectAsState()
        val showUpcomingPhaseDetails by settingsViewModel.showUpcomingPhaseDetailsStateFlow.collectAsState()

        fun toggleLunarPhase(shouldShow: Boolean) {
            if (shouldShow) settingsViewModel.showLunarPhase()
            else settingsViewModel.hideLunarPhase()
        }

        val locationPermissionsState = rememberMultiplePermissionsState(
            permissions = listOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            onPermissionsResult = { permissionsResult ->
                val areAllPermissionsGranted = permissionsResult.values.all { it }
                toggleLunarPhase(shouldShow = areAllPermissionsGranted)
            }
        )

        val isLocationPermissionGranted by derivedStateOf { locationPermissionsState.allPermissionsGranted }
        val isLunarPhaseEnabled by derivedStateOf { isLocationPermissionGranted && showLunarPhase }

        onLifecycleEventChange { event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                toggleLunarPhase(shouldShow = isLocationPermissionGranted)
            }
        }

        Column {
            PreviewLunarCalendar(
                settingsViewModel = settingsViewModel,
                widgetsViewModel = widgetsViewModel,
                horizontalPadding = horizontalPadding,
                showLunarPhase = isLunarPhaseEnabled,
            )
            SettingsSelectableSwitchItem(
                text = "Enable Lunar Phase",
                horizontalPadding = horizontalPadding,
                checked = isLunarPhaseEnabled,
                disabled = isLocationPermissionGranted.not(),
                onClick = { toggleLunarPhase(shouldShow = showLunarPhase.not()) }
            )
            AnimatedVisibility(visible = isLocationPermissionGranted.not()) {
                LocationPermissionRequiredNote(
                    horizontalPadding = horizontalPadding,
                    message = buildString {
                        val show = locationPermissionsState.shouldShowRationale
                        show.not()
                        append("Location permission is required for this feature.")
                    },
                    onClick = { locationPermissionsState.launchMultiplePermissionRequest() }
                )
            }
            AnimatedVisibility(visible = isLocationPermissionGranted) {
                SettingsSelectableSwitchItem(
                    text = "Show Illumination Percent",
                    horizontalPadding = horizontalPadding,
                    checked = showIlluminationPercent,
                    disabled = !showLunarPhase,
                    onClick = { settingsViewModel.toggleShowIlluminationPercent() }
                )
            }
            AnimatedVisibility(visible = isLocationPermissionGranted) {
                SettingsSelectableSwitchItem(
                    text = "Show Upcoming Phase Details",
                    horizontalPadding = horizontalPadding,
                    checked = showUpcomingPhaseDetails,
                    disabled = !showLunarPhase,
                    onClick = { settingsViewModel.toggleShowUpcomingPhaseDetails() }
                )
            }
            VerticalSpacer(spacing = bottomSpacing)
        }
    }
}

@Composable
private fun LocationPermissionRequiredNote(
    horizontalPadding: Dp,
    message: String,
    onClick: () -> Unit,
) {
    val backgroundColor = MaterialTheme.colors.secondaryVariant
    val textColor = MaterialTheme.colors.onBackground

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = 12.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_info),
            contentDescription = "Info Icon",
            tint = textColor
        )
        HorizontalSpacer(spacing = 14.dp)
        Text(
            text = message,
            style = TextStyle(
                color = textColor,
                lineHeight = 20.sp,
                letterSpacing = 1.sp
            )
        )
    }
}

@Composable
private fun PreviewLunarCalendar(
    settingsViewModel: SettingsViewModel,
    widgetsViewModel: WidgetsViewModel,
    showLunarPhase: Boolean,
    horizontalPadding: Dp = 24.dp,
) {
    val height = 74.dp

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
