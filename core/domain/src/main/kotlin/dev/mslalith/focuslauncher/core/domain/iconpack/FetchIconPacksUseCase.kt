package dev.mslalith.focuslauncher.core.domain.iconpack

import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import javax.inject.Inject

class FetchIconPacksUseCase @Inject constructor(
    private val iconPackManager: IconPackManager
) {
    operator fun invoke() = iconPackManager.fetchInstalledIconPacks()
}
