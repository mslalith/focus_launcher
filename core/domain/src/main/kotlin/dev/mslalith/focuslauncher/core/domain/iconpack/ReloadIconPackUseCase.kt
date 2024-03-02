package dev.mslalith.focuslauncher.core.domain.iconpack

import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReloadIconPackUseCase @Inject constructor(
    private val iconPackManager: IconPackManager,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) {
    suspend operator fun invoke() = withContext(appCoroutineDispatcher.io) {
        iconPackManager.reloadIconPack()
    }
}
