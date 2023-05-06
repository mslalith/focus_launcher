package dev.mslalith.focuslauncher.core.launcherapps.providers.icons

import android.graphics.drawable.Drawable
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.AppWithComponent

interface IconProvider {
    fun iconFor(appWithComponent: AppWithComponent, iconPackType: IconPackType): Drawable
}
