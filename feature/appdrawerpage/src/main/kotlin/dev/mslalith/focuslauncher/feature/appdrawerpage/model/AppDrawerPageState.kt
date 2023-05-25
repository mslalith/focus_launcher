package dev.mslalith.focuslauncher.feature.appdrawerpage.model

import dev.mslalith.focuslauncher.core.common.model.LoadingState
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.app.AppWithIconFavorite
import kotlinx.collections.immutable.ImmutableList

internal data class AppDrawerPageState(
    val allAppsState: LoadingState<ImmutableList<AppWithIconFavorite>>,
    val appDrawerViewType: AppDrawerViewType,
    val showAppIcons: Boolean,
    val showAppGroupHeader: Boolean,
    val showSearchBar: Boolean,
    val searchBarQuery: String
)
