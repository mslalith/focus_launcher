package dev.mslalith.focuslauncher.core.launcherapps.parser

import android.graphics.drawable.Drawable

internal interface IconPackParser {
    val packageName: String

    fun load()
    fun drawableFor(componentName: String): Drawable?
}
