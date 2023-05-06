package dev.mslalith.focuslauncher.core.domain.apps.core

import dev.mslalith.focuslauncher.core.domain.extensions.toAppWithIcons
import dev.mslalith.focuslauncher.core.domain.extensions.toAppsWithColor
import dev.mslalith.focuslauncher.core.domain.extensions.toAppsWithNoColor
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.IconProvider
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import dev.mslalith.focuslauncher.core.model.app.AppWithComponent
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetAppsUseCase @Inject constructor(
    private val launcherAppsManager: LauncherAppsManager,
    private val iconPackManager: IconPackManager,
    private val iconProvider: IconProvider
) {
    fun appsWithIcons(
        appsFlow: Flow<List<App>>,
        iconPackType: IconPackType
    ): Flow<List<AppWithIcon>> = appsFlow.flatMapLatest {
        appsWithComponents(appsFlow = appsFlow)
    }.mapLatest { appsWithComponents ->
        iconPackManager.loadIconPack(iconPackType = iconPackType)
        with(iconProvider) { appsWithComponents.toAppWithIcons(iconPackType = iconPackType) }
    }

    fun appsWithComponents(
        appsFlow: Flow<List<App>>
    ): Flow<List<AppWithComponent>> = appsFlow.mapLatest { apps ->
        apps.mapNotNull { launcherAppsManager.loadApp(packageName = it.packageName) }
    }

    fun appsWithColor(
        appsFlow: Flow<List<App>>,
        iconPackType: IconPackType
    ): Flow<List<AppWithColor>> = appsFlow.transformLatest { apps ->
        emit(value = apps.toAppsWithNoColor())

        emitAll(
            flow = appsWithIcons(
                appsFlow = appsFlow,
                iconPackType = iconPackType
            ).mapLatest(List<AppWithIcon>::toAppsWithColor)
        )
    }
}
