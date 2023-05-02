package dev.mslalith.focuslauncher.core.domain.extensions

import dev.mslalith.focuslauncher.core.domain.model.GetAppsState
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

internal fun Flow<GetAppsState>.filterAppsWithIconsState(): Flow<List<AppWithIcon>> =
    filterIsInstance<GetAppsState.AppsWithIconsLoaded>()
        .map { it.appsWithIcons }
