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
import kotlinx.coroutines.flow.onEach

@HiltViewModel
internal class IconPackViewModel @Inject constructor(
    private val iconPackManager: IconPackManager,
    private val iconProvider: IconProvider,
    private val generalSettingsRepo: GeneralSettingsRepo,
    appDrawerRepo: AppDrawerRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    private val _iconPackType = MutableStateFlow<IconPackType?>(value = null)

    private val defaultIconPackState = IconPackState(
        allApps = emptyList(),
        iconPacks = emptyList(),
        iconPackType = _iconPackType.value
    )

    init {
        iconPackManager.fetchInstalledIconPacks()
        generalSettingsRepo.iconPackTypeFlow
            .onEach { _iconPackType.value = it }
            .launchIn(scope = viewModelScope)
    }

    private val allAppsIconPackAware: Flow<List<AppWithIcon>> = _iconPackType
        .combine(flow = appDrawerRepo.allAppsFlow) { iconPackType, allApps ->
            iconPackType ?: return@combine emptyList()
            iconPackManager.loadIconPack(iconPackType = iconPackType)
            with(iconProvider) { allApps.toAppWithIcons(iconPackType = iconPackType) }
        }

    private val iconPackApps: Flow<List<App>> = appDrawerRepo.allAppsFlow
        .combine(flow = iconPackManager.iconPacksFlow) { allApps, iconPacks ->
            allApps.filter { app ->
                iconPacks.any { it.packageName == app.packageName }
            }
        }

    val iconPackState = flowOf(value = defaultIconPackState)
        .combine(flow = allAppsIconPackAware) { state, allApps ->
            state.copy(allApps = allApps)
        }.combine(flow = iconPackApps) { state, iconPackApps ->
            val iconPackType = generalSettingsRepo.iconPackTypeFlow.first()
            state.copy(iconPacks = with(iconProvider) { iconPackApps.toAppWithIcons(iconPackType) })
        }.combine(flow = _iconPackType) { state, iconPackType ->
            state.copy(iconPackType = iconPackType)
        }.withinScope(initialValue = defaultIconPackState)

    fun updateSelectedIconPackApp(iconPackApp: AppWithIcon) {
        val iconPackType = IconPackType.Custom(packageName = iconPackApp.packageName)
        _iconPackType.value = iconPackType
        appCoroutineDispatcher.launchInIO {
            iconPackManager.loadIconPack(iconPackType = iconPackType)
        }
    }

    fun saveIconPackType() {
        appCoroutineDispatcher.launchInIO {
            val iconPackType = _iconPackType.value ?: return@launchInIO
            generalSettingsRepo.updateIconPackType(iconPackType = iconPackType)
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
