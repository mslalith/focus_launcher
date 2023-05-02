package dev.mslalith.focuslauncher.core.domain.appswithicons

import dev.mslalith.focuslauncher.core.domain.extensions.filterAppsWithIconsState
import dev.mslalith.focuslauncher.core.domain.iconpack.GetIconPackAppsUseCase
import dev.mslalith.focuslauncher.core.model.AppWithIcon
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetIconPackAppsWithIconsUseCase @Inject internal constructor(
    private val getAppsStateGivenAppsUseCase: GetAppsStateGivenAppsUseCase,
    private val getIconPackAppsUseCase: GetIconPackAppsUseCase
) {
    operator fun invoke(): Flow<List<AppWithIcon>> = getAppsStateGivenAppsUseCase(
        appsFlow = getIconPackAppsUseCase()
    ).filterAppsWithIconsState()
}
