package dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.impl

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconcache.IconCacheManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import dev.mslalith.focuslauncher.core.launcherapps.model.IconPack
import dev.mslalith.focuslauncher.core.launcherapps.parser.IconPackXmlParser
import dev.mslalith.focuslauncher.core.model.IconPackLoadEvent
import dev.mslalith.focuslauncher.core.model.IconPackType
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

internal class IconPackManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val iconCacheManager: IconCacheManager
) : IconPackManager {

    private var currentIconPackType: IconPackType? = null
    private var currentIconPackParser: IconPackXmlParser? = null

    private val _iconPacksFlow = MutableStateFlow<List<IconPack>>(value = emptyList())
    override val iconPacksFlow: Flow<List<IconPack>> = _iconPacksFlow

    private val _iconPackLoadEventFlow = MutableSharedFlow<IconPackLoadEvent>(replay = 1)
    override val iconPackLoadEventFlow: Flow<IconPackLoadEvent> = _iconPackLoadEventFlow

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

    override suspend fun loadIconPack(iconPackType: IconPackType) = loadIconPack(
        iconPackType = iconPackType,
        forceLoad = false
    )

    override fun reloadIconPack() {
        val iconPackType = currentIconPackType ?: return
        loadIconPack(
            iconPackType = iconPackType,
            forceLoad = true
        )
    }

    private fun loadIconPack(iconPackType: IconPackType, forceLoad: Boolean) {
        when (iconPackType) {
            is IconPackType.Custom -> loadCustomTypeIcons(iconPackType = iconPackType, forceLoad = forceLoad)
            IconPackType.System -> loadSystemTypeIcons(forceLoad = forceLoad)
        }
    }

    private fun loadSystemTypeIcons(forceLoad: Boolean) {
        if (!forceLoad && currentIconPackType == IconPackType.System) return

        _iconPackLoadEventFlow.updateStartLoad(forceLoad =  forceLoad)
        currentIconPackType = IconPackType.System
        iconCacheManager.clearCache()
        currentIconPackParser = null
        _iconPackLoadEventFlow.updateEndLoad(forceLoad =  forceLoad)
    }

    private fun loadCustomTypeIcons(iconPackType: IconPackType.Custom, forceLoad: Boolean) {
        if (!forceLoad && currentIconPackParser?.packageName == iconPackType.packageName) return

        _iconPackLoadEventFlow.updateStartLoad(forceLoad =  forceLoad)
        currentIconPackType = iconPackType
        iconCacheManager.clearCache()
        currentIconPackParser = iconCacheManager.iconPackFor(packageName = iconPackType.packageName)
        currentIconPackParser?.load()
        _iconPackLoadEventFlow.updateEndLoad(forceLoad =  forceLoad)
    }
}

private fun MutableSharedFlow<IconPackLoadEvent>.updateStartLoad(forceLoad: Boolean) {
    tryEmit(
        value = when (forceLoad) {
            true -> IconPackLoadEvent.Reloading
            false -> IconPackLoadEvent.Loading
        }
    )
}

private fun MutableSharedFlow<IconPackLoadEvent>.updateEndLoad(forceLoad: Boolean) {
    tryEmit(
        value = when (forceLoad) {
            true -> IconPackLoadEvent.Reloaded
            false -> IconPackLoadEvent.Loaded
        }
    )
}
