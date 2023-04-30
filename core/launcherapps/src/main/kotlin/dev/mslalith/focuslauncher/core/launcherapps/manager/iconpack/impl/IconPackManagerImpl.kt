package dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.impl

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconcache.IconCacheManager
import dev.mslalith.focuslauncher.core.launcherapps.model.IconPack
import dev.mslalith.focuslauncher.core.launcherapps.parser.IconPackXmlParser
import dev.mslalith.focuslauncher.core.model.IconPackType
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

internal class IconPackManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val iconCacheManager: IconCacheManager
) : IconPackManager {

    private var currentIconPackParser: IconPackXmlParser? = null

    private val _iconPacksFlow = MutableStateFlow<List<IconPack>>(value = emptyList())
    override val iconPacksFlow: Flow<List<IconPack>> = _iconPacksFlow

    private val _iconPackLoadedTriggerFlow = MutableStateFlow(value = false)
    override val iconPackLoadedTriggerFlow: Flow<Boolean> = _iconPackLoadedTriggerFlow

    override fun fetchInstalledIconPacks() {
        val packageManager = context.packageManager
        val themes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(Intent("org.adw.launcher.THEMES"), PackageManager.ResolveInfoFlags.of(PackageManager.GET_META_DATA.toLong()))
        } else {
            @Suppress("DEPRECATION")
            packageManager.queryIntentActivities(Intent("org.adw.launcher.THEMES"), PackageManager.GET_META_DATA)
        }

        _iconPacksFlow.value = themes.mapNotNull {
            val iconPackageName = it.activityInfo.packageName
            try {
                val applicationInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    packageManager.getApplicationInfo(iconPackageName, PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong()))
                } else {
                    @Suppress("DEPRECATION")
                    packageManager.getApplicationInfo(iconPackageName, PackageManager.GET_META_DATA)
                }
                val iconLabel = packageManager.getApplicationLabel(applicationInfo).toString()
                IconPack(
                    label = iconLabel,
                    packageName = iconPackageName
                )
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
        }
    }

    override suspend fun loadIconPack(iconPackType: IconPackType) {
        when (iconPackType) {
            is IconPackType.Custom -> loadCustomTypeIcons(packageName = iconPackType.packageName)
            IconPackType.System -> loadSystemTypeIcons()
        }
    }

    private fun loadSystemTypeIcons() {
        iconCacheManager.clearCache()
        currentIconPackParser = null
        _iconPackLoadedTriggerFlow.update { !it }
    }

    private fun loadCustomTypeIcons(packageName: String) {
        if (currentIconPackParser?.packageName == packageName) return

        iconCacheManager.clearCache()
        currentIconPackParser = iconCacheManager.iconPackFor(packageName = packageName)
        currentIconPackParser?.load()
        _iconPackLoadedTriggerFlow.update { !it }
    }
}
