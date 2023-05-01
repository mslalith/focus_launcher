package dev.mslalith.focuslauncher.core.domain.appswithicons

import android.content.pm.PackageManager
import dev.mslalith.focuslauncher.core.domain.model.GetAppsState
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.IconProvider
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.model.AppWithIcon
import dev.mslalith.focuslauncher.core.model.IconPackType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

class GetAppsStateGivenAppsAndIconPackTypeUseCase @Inject constructor(
    private val iconPackManager: IconPackManager,
    private val iconProvider: IconProvider
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        appsFlow: Flow<List<App>>,
        iconPackType: IconPackType
    ): Flow<GetAppsState> = appsFlow.transformLatest { apps ->
        emit(value = GetAppsState.AppsLoaded(apps = apps))

        iconPackManager.loadIconPack(iconPackType = iconPackType)
        val appsWithIcons = with(iconProvider) { apps.toAppWithIcons(iconPackType = iconPackType) }

        emit(value = GetAppsState.AppsWithIconsLoaded(appsWithIcons = appsWithIcons))
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
