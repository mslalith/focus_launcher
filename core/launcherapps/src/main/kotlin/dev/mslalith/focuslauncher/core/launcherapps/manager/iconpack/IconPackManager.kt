package dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack

import dev.mslalith.focuslauncher.core.launcherapps.model.IconPack

interface IconPackManager {
    fun fetchInstalledIconPacks(): List<IconPack>
}
