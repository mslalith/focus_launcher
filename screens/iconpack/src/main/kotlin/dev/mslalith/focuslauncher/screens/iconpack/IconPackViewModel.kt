package dev.mslalith.focuslauncher.screens.iconpack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.IconProvider
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon
import dev.mslalith.focuslauncher.screens.iconpack.model.IconPackState
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@HiltViewModel
internal class IconPackViewModel @Inject constructor(
    private val iconPackManager: IconPackManager,
    private val iconProvider: IconProvider,
    private val generalSettingsRepo: GeneralSettingsRepo,
    appDrawerRepo: AppDrawerRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    private val _iconPackApp = MutableStateFlow<AppWithIcon?>(value = null)

    private val defaultIconPackState = IconPackState(
        allApps = emptyList(),
        iconPacks = emptyList(),
        iconPackApp = _iconPackApp.value,
    )

    init {
        iconPackManager.fetchInstalledIconPacks()
        generalSettingsRepo.iconPackTypeFlow
            .onEach { iconPackManager.loadIconPack(iconPackType = it) }
            .launchIn(scope = viewModelScope)
    }

    private val allAppsWithIcon: Flow<List<AppWithIcon>> = appDrawerRepo.allAppsFlow
        .map { allApps ->
            val iconPackType = generalSettingsRepo.iconPackTypeFlow.first()
            with(iconProvider) { allApps.toAppWithIcons(iconPackType) }
        }

    private val iconPackApps: Flow<List<App>> = appDrawerRepo.allAppsFlow
        .combine(flow = iconPackManager.iconPacksFlow) { allApps, iconPacks ->
            allApps.filter { app ->
                iconPacks.any { it.packageName == app.packageName }
            }
        }

    val iconPackState = flowOf(value = defaultIconPackState)
        .combine(flow = iconPackManager.iconPackLoadedTriggerFlow) { state, _ ->
            val iconPackType = generalSettingsRepo.iconPackTypeFlow.first()
            val allApps = appDrawerRepo.allAppsFlow.first()
            val allAppsWithIcons = with(iconProvider) { allApps.toAppWithIcons(iconPackType = iconPackType) }
            state.copy(allApps = allAppsWithIcons)
        }
        .combine(flow = iconPackApps) { state, iconPackApps ->
            val iconPackType = generalSettingsRepo.iconPackTypeFlow.first()
            state.copy(iconPacks = with(iconProvider) { iconPackApps.toAppWithIcons(iconPackType) })
        }.combine(flow = _iconPackApp) { state, iconPackApp ->
            state.copy(iconPackApp = iconPackApp)
        }.withinScope(initialValue = defaultIconPackState)

    fun updateSelectedIconPackApp(iconPackApp: AppWithIcon) {
        _iconPackApp.value = iconPackApp
        appCoroutineDispatcher.launchInIO {
            iconPackManager.loadIconPack(iconPackType = IconPackType.Custom(packageName = iconPackApp.packageName))
        }
    }

    fun saveIconPackType() {
        appCoroutineDispatcher.launchInIO {
            val packageName = _iconPackApp.value?.packageName ?: return@launchInIO
            generalSettingsRepo.updateIconPackType(iconPackType = IconPackType.Custom(packageName = packageName))
        }
    }
}

context (IconProvider)
private fun List<App>.toAppWithIcons(iconPackType: IconPackType): List<AppWithIcon> = map { app ->
    AppWithIcon(
        name = app.name,
        displayName = app.displayName,
        packageName = app.packageName,
        icon = iconFor(app.packageName, iconPackType),
        isSystem = app.isSystem
    )
}
