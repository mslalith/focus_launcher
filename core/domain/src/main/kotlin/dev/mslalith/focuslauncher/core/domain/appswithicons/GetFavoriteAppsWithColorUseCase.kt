package dev.mslalith.focuslauncher.core.domain.appswithicons

import android.content.pm.PackageManager
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColor
import androidx.palette.graphics.Palette
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.IconProvider
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.model.AppWithColor
import dev.mslalith.focuslauncher.core.model.AppWithIcon
import dev.mslalith.focuslauncher.core.model.IconPackType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

class GetFavoriteAppsWithColorUseCase @Inject constructor(
    private val iconPackManager: IconPackManager,
    private val iconProvider: IconProvider,
    private val generalSettingsRepo: GeneralSettingsRepo,
    private val favoritesRepo: FavoritesRepo
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<AppWithColor>> = favoritesRepo.onlyFavoritesFlow
        .combine(flow = generalSettingsRepo.iconPackTypeFlow) { favorites, iconPackType ->
            favorites to iconPackType
        }
        .transformLatest { (favorites, iconPackType) ->
            emit(favorites.map { AppWithColor(app = it, color = null) })

            iconPackManager.loadIconPack(iconPackType = iconPackType)
            val appsWithIcons = with(iconProvider) { favorites.toAppWithIcons(iconPackType = iconPackType) }

            appsWithIcons.map { app ->
                val appIconPalette = Palette.from(app.icon.toBitmap()).generate()
                val extractedColor = appIconPalette.dominantSwatch?.rgb?.toColor()
                AppWithColor(
                    app = app.toApp(),
                    color = extractedColor
                )
            }.let { emit(it) }
        }

    context (IconProvider)
    private fun List<App>.toAppWithIcons(iconPackType: IconPackType): List<AppWithIcon> = mapNotNull { app ->
        try {
            AppWithIcon(
                name = app.name,
                displayName = app.displayName,
                packageName = app.packageName,
                icon = iconFor(app.packageName, iconPackType),
                isSystem = app.isSystem
            )
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }
}
