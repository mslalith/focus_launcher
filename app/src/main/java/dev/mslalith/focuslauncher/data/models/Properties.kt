package dev.mslalith.focuslauncher.data.models

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.data.model.AppWithIcon
import dev.mslalith.focuslauncher.ui.viewmodels.AppsViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.SettingsViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.WidgetsViewModel

private val BOTTOM_SHEET_BOTTOM_SPACING = 12.dp

data class ConfirmDialogProperties(
    val title: String,
    val message: String,
    val confirmButtonText: String = "Confirm",
    val cancelButtonText: String = "Cancel",
    val onConfirm: () -> Unit,
    val onCancel: () -> Unit,
)

data class AppDrawerSettingsProperties(
    val appsViewModel: AppsViewModel,
    val settingsViewModel: SettingsViewModel,
    val bottomSpacing: Dp = BOTTOM_SHEET_BOTTOM_SPACING,
)

data class MoreAppOptionsProperties(
    val appsViewModel: AppsViewModel,
    val settingsViewModel: SettingsViewModel,
    val app: AppWithIcon,
    val bottomSpacing: Dp = BOTTOM_SHEET_BOTTOM_SPACING,
    val onUpdateDisplayNameClick: () -> Unit,
    val onClose: () -> Unit,
)

data class ClockSettingsProperties(
    val widgetsViewModel: WidgetsViewModel,
    val settingsViewModel: SettingsViewModel,
    val bottomSpacing: Dp = BOTTOM_SHEET_BOTTOM_SPACING,
)

data class LunarPhaseSettingsProperties(
    val widgetsViewModel: WidgetsViewModel,
    val settingsViewModel: SettingsViewModel,
    val bottomSpacing: Dp = BOTTOM_SHEET_BOTTOM_SPACING,
)

data class QuotesSettingsProperties(
    val widgetsViewModel: WidgetsViewModel,
    val settingsViewModel: SettingsViewModel,
    val bottomSpacing: Dp = BOTTOM_SHEET_BOTTOM_SPACING,
)
