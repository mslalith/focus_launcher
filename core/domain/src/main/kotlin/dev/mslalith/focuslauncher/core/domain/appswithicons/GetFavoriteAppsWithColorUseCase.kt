package dev.mslalith.focuslauncher.core.domain.appswithicons

import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColor
import androidx.palette.graphics.Palette
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.domain.model.GetAppsState
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

class GetFavoriteAppsWithColorUseCase @Inject internal constructor(
    private val getAppsStateGivenAppsUseCase: GetAppsStateGivenAppsUseCase,
    private val favoritesRepo: FavoritesRepo
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<AppWithColor>> = getAppsStateGivenAppsUseCase(
        appsFlow = favoritesRepo.onlyFavoritesFlow
    ).transformLatest { getAppsState ->
        when (getAppsState) {
            is GetAppsState.AppsLoaded -> emit(value = getAppsState.apps.toAppsWithNoColor())
            is GetAppsState.AppsWithIconsLoaded -> emit(value = getAppsState.appsWithIcons.toAppsWithColor())
        }
    }

    private fun List<App>.toAppsWithNoColor(): List<AppWithColor> = map { app ->
        AppWithColor(
            app = app,
            color = null
        )
    }

    private fun List<AppWithIcon>.toAppsWithColor(): List<AppWithColor> = map { app ->
        val appIconPalette = Palette.from(app.icon.toBitmap()).generate()
        val extractedColor = appIconPalette.dominantSwatch?.rgb?.toColor()
        AppWithColor(
            app = app.toApp(),
            color = extractedColor
        )
    }
}
