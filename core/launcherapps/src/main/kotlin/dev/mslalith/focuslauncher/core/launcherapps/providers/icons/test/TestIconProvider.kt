package dev.mslalith.focuslauncher.core.launcherapps.providers.icons.test

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.IconProvider
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.AppWithComponent

class TestIconProvider : IconProvider {
    override fun iconFor(appWithComponent: AppWithComponent, iconPackType: IconPackType): Drawable {
        return ColorDrawable("${iconPackType.value}${appWithComponent.app.packageName}".hashCode())
    }
}
