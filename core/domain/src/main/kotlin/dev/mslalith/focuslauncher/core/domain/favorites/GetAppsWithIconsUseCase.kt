package dev.mslalith.focuslauncher.core.domain.favorites

import android.content.pm.PackageManager
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.IconProvider
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.model.AppWithIcon
import dev.mslalith.focuslauncher.core.model.IconPackType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetAppsWithIconsUseCase @Inject constructor(
    private val iconPackManager: IconPackManager,
    private val iconProvider: IconProvider,
    private val generalSettingsRepo: GeneralSettingsRepo
) {
    operator fun invoke(appsFlow: Flow<List<App>>): Flow<List<AppWithIcon>> = generalSettingsRepo.iconPackTypeFlow
        .combine(flow = appsFlow) { iconPackType, apps ->
            iconPackManager.loadIconPack(iconPackType = iconPackType)
            with(iconProvider) { apps.toAppWithIcons(iconPackType = iconPackType) }
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
