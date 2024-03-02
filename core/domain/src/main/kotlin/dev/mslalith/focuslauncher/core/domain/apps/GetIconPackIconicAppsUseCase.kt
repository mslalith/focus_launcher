package dev.mslalith.focuslauncher.core.domain.apps

import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.domain.apps.core.GetAppsIconPackAwareUseCase
import dev.mslalith.focuslauncher.core.domain.iconpack.GetIconPackAppsUseCase
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetIconPackIconicAppsUseCase @Inject internal constructor(
    private val getAppsIconPackAwareUseCase: GetAppsIconPackAwareUseCase,
    private val getIconPackAppsUseCase: GetIconPackAppsUseCase,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) {
    operator fun invoke(): Flow<List<AppWithIcon>> = getAppsIconPackAwareUseCase.appsWithIcons(
        appsFlow = getIconPackAppsUseCase()
    ).flowOn(context = appCoroutineDispatcher.io)
}
