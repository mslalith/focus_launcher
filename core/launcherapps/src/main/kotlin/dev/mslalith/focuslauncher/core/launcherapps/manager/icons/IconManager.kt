package dev.mslalith.focuslauncher.core.launcherapps.manager.icons

import android.graphics.drawable.Drawable
import dev.mslalith.focuslauncher.core.launcherapps.parser.IconPackXmlParser
import dev.mslalith.focuslauncher.core.model.IconPackType

internal interface IconManager {
    fun addToCache(packageName: String, drawable: Drawable)
    fun clearCache()
    fun iconPackFor(packageName: String): IconPackXmlParser
    fun iconFor(packageName: String): Drawable
    fun iconFor(packageName: String, iconPackType: IconPackType): Drawable
}
