package dev.mslalith.focuslauncher.feature.appdrawerpage.model

import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType

internal data class AppDrawerPageState(
    val allApps: List<App>,
    val appDrawerViewType: AppDrawerViewType,
    val showAppIcons: Boolean,
    val showAppGroupHeader: Boolean,
    val showSearchBar: Boolean,
    val searchBarQuery: String
)
