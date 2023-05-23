package dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.test

import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import dev.mslalith.focuslauncher.core.launcherapps.model.IconPack
import dev.mslalith.focuslauncher.core.model.IconPackLoadEvent
import dev.mslalith.focuslauncher.core.model.IconPackType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class TestIconPackManager : IconPackManager {
    private val _iconPacksFlow = MutableStateFlow<List<IconPack>>(value = emptyList())
    override val iconPacksFlow: Flow<List<IconPack>> = _iconPacksFlow

    private val _iconPackLoadEventFlow = MutableSharedFlow<IconPackLoadEvent>(replay = 1)
    override val iconPackLoadEventFlow: Flow<IconPackLoadEvent> = _iconPackLoadEventFlow

    override fun fetchInstalledIconPacks() = Unit

    override suspend fun loadIconPack(iconPackType: IconPackType) {
        _iconPackLoadEventFlow.tryEmit(value = IconPackLoadEvent.Loading)
        delay(timeMillis = 2000)
        _iconPackLoadEventFlow.tryEmit(value = IconPackLoadEvent.Loaded)
    }

    override fun reloadIconPack() = Unit

    fun setIconPackApps(iconPacks: List<IconPack>) {
        _iconPacksFlow.value = iconPacks
    }
}
