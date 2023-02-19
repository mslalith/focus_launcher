package dev.mslalith.focuslauncher.data.models

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.ui.viewmodels.AppsViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.SettingsViewModel

data class MoreAppOptionsProperties(
    val appsViewModel: AppsViewModel,
    val settingsViewModel: SettingsViewModel,
    val app: AppWithIcon,
    val bottomSpacing: Dp = 12.dp,
    val onUpdateDisplayNameClick: () -> Unit,
    val onClose: () -> Unit
)
