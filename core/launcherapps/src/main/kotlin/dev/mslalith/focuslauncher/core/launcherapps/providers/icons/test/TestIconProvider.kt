package dev.mslalith.focuslauncher.core.launcherapps.providers.icons.test

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.IconProvider
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.AppWithComponent

object TestIconProvider : IconProvider {

    private var drawable = ColorDrawable(Color.WHITE)

    fun setIconColor(color: Int) {
        drawable = ColorDrawable(color)
    }

    override fun iconFor(appWithComponent: AppWithComponent, iconPackType: IconPackType): Drawable = drawable
}
