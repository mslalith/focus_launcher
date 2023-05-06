package dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack

import dev.mslalith.focuslauncher.core.launcherapps.model.IconPack
import dev.mslalith.focuslauncher.core.model.IconPackType
import kotlinx.coroutines.flow.Flow

interface IconPackManager {
    val iconPacksFlow: Flow<List<IconPack>>
    val iconPackLoadedTriggerFlow: Flow<Boolean>

    fun fetchInstalledIconPacks()
    suspend fun loadIconPack(iconPackType: IconPackType)
    fun reloadIconPack()
}
