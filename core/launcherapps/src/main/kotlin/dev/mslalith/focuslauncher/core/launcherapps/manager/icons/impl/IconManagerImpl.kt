package dev.mslalith.focuslauncher.core.launcherapps.manager.icons.impl

import android.content.Context
import android.graphics.drawable.Drawable
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mslalith.focuslauncher.core.launcherapps.manager.icons.IconManager
import javax.inject.Inject

internal class IconManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : IconManager {

    private val iconCache = hashMapOf<String, Drawable>()

    override fun iconFor(packageName: String): Drawable = iconCache.getOrPut(packageName) {
        context.packageManager.getApplicationIcon(packageName)
    }
}
