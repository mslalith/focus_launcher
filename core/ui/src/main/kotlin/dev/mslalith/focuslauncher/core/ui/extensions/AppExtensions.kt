package dev.mslalith.focuslauncher.core.ui.extensions

import android.content.Context
import android.content.pm.PackageManager
import dev.mslalith.focuslauncher.core.common.extensions.iconOf
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon

fun App.toAppWithIcon(context: Context): AppWithIcon? = context.iconOf(packageName)?.let { icon ->
    AppWithIcon(
        name = name,
        displayName = displayName,
        packageName = packageName,
        icon = icon,
        isSystem = isSystem
    )
}

fun List<App>.toAppWithIconList(context: Context) =
    mapNotNull {
        try {
            it.toAppWithIcon(context)
        } catch (ex: PackageManager.NameNotFoundException) {
            null
        }
    }
