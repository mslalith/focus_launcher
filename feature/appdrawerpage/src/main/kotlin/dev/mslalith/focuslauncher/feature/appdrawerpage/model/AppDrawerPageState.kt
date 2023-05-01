package dev.mslalith.focuslauncher.feature.appdrawerpage.model

import dev.mslalith.focuslauncher.core.common.LoadingState
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.AppWithIcon

internal data class AppDrawerPageState(
    val allAppsState: LoadingState<List<AppWithIcon>>,
    val appDrawerViewType: AppDrawerViewType,
    val showAppIcons: Boolean,
    val showAppGroupHeader: Boolean,
    val showSearchBar: Boolean,
    val searchBarQuery: String
)
