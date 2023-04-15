package dev.mslalith.focuslauncher.core.launcherapps.manager.icons

import android.graphics.drawable.Drawable

internal interface IconManager {
    fun iconFor(packageName: String): Drawable
}
