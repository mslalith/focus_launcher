package dev.mslalith.focuslauncher.core.domain.model

import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.model.AppWithIcon

internal sealed interface GetAppsState {
    data class AppsLoaded(val apps: List<App>) : GetAppsState
    data class AppsWithIconsLoaded(val appsWithIcons: List<AppWithIcon>) : GetAppsState
}
