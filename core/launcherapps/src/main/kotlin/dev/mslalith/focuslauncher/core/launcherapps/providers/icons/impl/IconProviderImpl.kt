package dev.mslalith.focuslauncher.core.launcherapps.providers.icons.impl

import android.graphics.drawable.Drawable
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconcache.IconCacheManager
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.IconProvider
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.AppWithComponent
import javax.inject.Inject

internal class IconProviderImpl @Inject constructor(
    private val iconCacheManager: IconCacheManager
) : IconProvider {

    override fun iconFor(appWithComponent: AppWithComponent, iconPackType: IconPackType): Drawable = iconCacheManager.iconFor(
        appWithComponent = appWithComponent,
        iconPackType = iconPackType
    )
}
