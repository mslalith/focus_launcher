package dev.mslalith.focuslauncher.core.domain.apps

import dev.mslalith.focuslauncher.core.domain.apps.core.GetAppsIconPackAwareUseCase
import dev.mslalith.focuslauncher.core.domain.iconpack.GetIconPackAppsUseCase
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetIconPackAppsWithIconsUseCase @Inject internal constructor(
    private val getAppsIconPackAwareUseCase: GetAppsIconPackAwareUseCase,
    private val getIconPackAppsUseCase: GetIconPackAppsUseCase
) {
    operator fun invoke(): Flow<List<AppWithIcon>> = getAppsIconPackAwareUseCase.appsWithIcons(
        appsFlow = getIconPackAppsUseCase()
    )
}
