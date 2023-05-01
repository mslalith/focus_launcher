package dev.mslalith.focuslauncher.core.domain.iconpack

import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import dev.mslalith.focuslauncher.core.model.IconPackType
import javax.inject.Inject

class LoadIconPackUseCase @Inject constructor(
    private val iconPackManager: IconPackManager
) {
    suspend  operator fun invoke(iconPackType: IconPackType) = iconPackManager.loadIconPack(
        iconPackType = iconPackType
    )
}
