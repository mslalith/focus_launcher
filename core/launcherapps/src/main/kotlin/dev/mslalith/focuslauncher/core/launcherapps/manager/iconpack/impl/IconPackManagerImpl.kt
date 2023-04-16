package dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.impl

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.icons.IconManager
import dev.mslalith.focuslauncher.core.launcherapps.model.IconPack
import dev.mslalith.focuslauncher.core.launcherapps.parser.IconPackParser
import dev.mslalith.focuslauncher.core.launcherapps.parser.IconPackXmlParser
import javax.inject.Inject

internal class IconPackManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val iconManager: IconManager
) : IconPackManager {

    private var currentIconPackParser: IconPackParser? = null

    override fun fetchInstalledIconPacks(): List<IconPack> {
        val packageManager = context.packageManager
        val themes = packageManager.queryIntentActivities(Intent("org.adw.launcher.THEMES"), PackageManager.GET_META_DATA)

        return themes.mapNotNull { ri ->
            val iconPackageName = ri.activityInfo.packageName
            try {
                val applicationInfo = packageManager.getApplicationInfo(iconPackageName, PackageManager.GET_META_DATA)
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

    private fun loadIconPack(packageName: String?) {
        if (packageName == null || packageName.equals("default", ignoreCase = true)) {
            currentIconPackParser = null
            return
        }

        if (currentIconPackParser == null || currentIconPackParser?.packageName != packageName) {
            currentIconPackParser = IconPackXmlParser(context = context, iconPackPackageName = packageName)
            currentIconPackParser?.load()
        }
    }
}
