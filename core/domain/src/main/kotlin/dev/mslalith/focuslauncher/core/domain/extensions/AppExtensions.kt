package dev.mslalith.focuslauncher.core.domain.extensions

import android.content.pm.PackageManager
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColor
import androidx.palette.graphics.Palette
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.IconProvider
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon

context (IconProvider)
internal fun List<App>.toAppWithIcons(iconPackType: IconPackType): List<AppWithIcon> = mapNotNull { app ->
    try {
        AppWithIcon(
            app = App(
                name = app.name,
                displayName = app.displayName,
                packageName = app.packageName,
                isSystem = app.isSystem
            ),
            icon = iconFor(app.packageName, iconPackType),
        )
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
}

internal fun List<App>.toAppsWithNoColor(): List<AppWithColor> = map { app ->
    AppWithColor(
        app = app,
        color = null
    )
}

internal fun List<AppWithIcon>.toAppsWithColor(): List<AppWithColor> = map { appWithIcon ->
    val appIconPalette = Palette.from(appWithIcon.icon.toBitmap()).generate()
    val extractedColor = appIconPalette.dominantSwatch?.rgb?.toColor()
    AppWithColor(
        app = appWithIcon.app,
        color = extractedColor
    )
}
