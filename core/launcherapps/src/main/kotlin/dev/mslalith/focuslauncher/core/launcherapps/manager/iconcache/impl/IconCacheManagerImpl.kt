package dev.mslalith.focuslauncher.core.launcherapps.manager.iconcache.impl

import android.content.Context
import android.graphics.drawable.Drawable
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconcache.IconCacheManager
import dev.mslalith.focuslauncher.core.launcherapps.parser.IconPackXmlParser
import dev.mslalith.focuslauncher.core.launcherapps.utils.GoogleCalendarIcon
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.AppWithComponent
import javax.inject.Inject

internal class IconCacheManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : IconCacheManager {

    private val iconCache = hashMapOf<String, Drawable>()
    private val iconPackCache = hashMapOf<String, IconPackXmlParser>()

    override fun clearCache() {
        iconCache.clear()
    }

    override fun iconPackFor(packageName: String): IconPackXmlParser = iconPackCache.getOrPut(key = packageName) {
        IconPackXmlParser(context = context, iconPackPackageName = packageName)
    }

    override fun iconFor(appWithComponent: AppWithComponent, iconPackType: IconPackType): Drawable = when (iconPackType) {
        is IconPackType.Custom -> getCustomTypeIcon(iconPackPackageName = iconPackType.packageName, appWithComponent = appWithComponent)
        IconPackType.System -> getSystemTypeIcon(appWithComponent = appWithComponent)
    }

    private fun getSystemTypeIcon(appWithComponent: AppWithComponent): Drawable = iconCache.getOrPut(key = appWithComponent.app.packageName) {
        if (appWithComponent.app.packageName == GoogleCalendarIcon.GOOGLE_CALENDAR_PACKAGE_NAME) {
            val drawable = GoogleCalendarIcon.getDrawable(context = context, activityName = appWithComponent.componentName.className)
            if (drawable != null) return@getOrPut drawable
        }
        context.packageManager.getApplicationIcon(appWithComponent.app.packageName)
    }

    private fun getCustomTypeIcon(iconPackPackageName: String, appWithComponent: AppWithComponent): Drawable = iconCache.getOrPut(key = appWithComponent.app.packageName) {
        val componentName = context.packageManager.getLaunchIntentForPackage(appWithComponent.app.packageName)?.component
        val key = componentName?.toString() ?: appWithComponent.app.packageName
        val iconPack = iconPackFor(packageName = iconPackPackageName)
        iconPack.drawableFor(componentName = key) ?: getSystemTypeIcon(appWithComponent = appWithComponent)
    }
}
