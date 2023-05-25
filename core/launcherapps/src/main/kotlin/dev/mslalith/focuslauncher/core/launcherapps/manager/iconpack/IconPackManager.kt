package dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack

import dev.mslalith.focuslauncher.core.launcherapps.model.IconPack
import dev.mslalith.focuslauncher.core.model.IconPackLoadEvent
import dev.mslalith.focuslauncher.core.model.IconPackType
import kotlinx.coroutines.flow.Flow

interface IconPackManager {
    val iconPacksFlow: Flow<List<IconPack>>
    val iconPackLoadEventFlow: Flow<IconPackLoadEvent>

    fun fetchInstalledIconPacks()
    suspend fun loadIconPack(iconPackType: IconPackType)
    suspend fun reloadIconPack()
}
