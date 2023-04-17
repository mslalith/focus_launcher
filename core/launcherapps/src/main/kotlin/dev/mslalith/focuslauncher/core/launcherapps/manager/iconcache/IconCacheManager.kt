package dev.mslalith.focuslauncher.core.launcherapps.manager.iconcache

import android.graphics.drawable.Drawable
import dev.mslalith.focuslauncher.core.launcherapps.parser.IconPackXmlParser
import dev.mslalith.focuslauncher.core.model.IconPackType

internal interface IconCacheManager {
    fun clearCache()
    fun iconPackFor(packageName: String): IconPackXmlParser
    fun iconFor(packageName: String, iconPackType: IconPackType): Drawable
}
