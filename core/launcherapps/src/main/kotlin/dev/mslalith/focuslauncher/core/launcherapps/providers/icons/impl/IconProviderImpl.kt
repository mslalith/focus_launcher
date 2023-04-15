package dev.mslalith.focuslauncher.core.launcherapps.providers.icons.impl

import android.graphics.drawable.Drawable
import dev.mslalith.focuslauncher.core.launcherapps.manager.icons.IconManager
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.IconProvider
import javax.inject.Inject

internal class IconProviderImpl @Inject constructor(
    private val iconManager: IconManager
) : IconProvider {
    override fun iconFor(packageName: String): Drawable = iconManager.iconFor(packageName)
}
