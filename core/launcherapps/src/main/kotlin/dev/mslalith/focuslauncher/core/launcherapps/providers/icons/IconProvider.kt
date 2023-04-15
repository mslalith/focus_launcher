package dev.mslalith.focuslauncher.core.launcherapps.providers.icons

import android.graphics.drawable.Drawable

interface IconProvider {
    fun iconFor(packageName: String): Drawable
}
