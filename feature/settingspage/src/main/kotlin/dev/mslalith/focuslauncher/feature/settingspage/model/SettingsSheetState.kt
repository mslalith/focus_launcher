package dev.mslalith.focuslauncher.feature.settingspage.model

import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerIconViewType

data class SettingsSheetState(
    val appDrawerViewType: AppDrawerViewType,
    val appDrawerIconViewType: AppDrawerIconViewType,
    val showAppGroupHeader: Boolean,
    val showSearchBar: Boolean
)
