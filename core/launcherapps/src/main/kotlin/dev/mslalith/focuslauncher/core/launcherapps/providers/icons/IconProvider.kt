package dev.mslalith.focuslauncher.core.launcherapps.providers.icons

import android.graphics.drawable.Drawable
import dev.mslalith.focuslauncher.core.model.IconPackType

interface IconProvider {
    fun iconFor(packageName: String, iconPackType: IconPackType): Drawable
}
