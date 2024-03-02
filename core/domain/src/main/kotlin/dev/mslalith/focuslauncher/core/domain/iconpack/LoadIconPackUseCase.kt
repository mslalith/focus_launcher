package dev.mslalith.focuslauncher.core.domain.iconpack

import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import dev.mslalith.focuslauncher.core.model.IconPackType
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadIconPackUseCase @Inject constructor(
    private val iconPackManager: IconPackManager,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) {
    suspend operator fun invoke(iconPackType: IconPackType) = withContext(appCoroutineDispatcher.io) {
        iconPackManager.loadIconPack(
            iconPackType = iconPackType
        )
    }
}
