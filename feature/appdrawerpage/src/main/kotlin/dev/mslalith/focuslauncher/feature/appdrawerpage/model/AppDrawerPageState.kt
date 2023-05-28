package dev.mslalith.focuslauncher.feature.appdrawerpage.model

import dev.mslalith.focuslauncher.core.common.model.LoadingState
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerIconViewType
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import kotlinx.collections.immutable.ImmutableList

internal data class AppDrawerPageState(
    val allAppsState: LoadingState<ImmutableList<AppDrawerItem>>,
    val appDrawerViewType: AppDrawerViewType,
    val appDrawerIconViewType: AppDrawerIconViewType,
    val showAppGroupHeader: Boolean,
    val showSearchBar: Boolean,
    val searchBarQuery: String
)
