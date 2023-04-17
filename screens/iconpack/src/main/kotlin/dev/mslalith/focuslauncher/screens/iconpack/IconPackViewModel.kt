package dev.mslalith.focuslauncher.screens.iconpack

import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.LoadingState
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class IconPackViewModel @Inject constructor(
    private val iconPackManager: IconPackManager,
    private val iconProvider: IconProvider,
    private val generalSettingsRepo: GeneralSettingsRepo,
    private val appDrawerRepo: AppDrawerRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    private val _iconPackType = MutableStateFlow<IconPackType?>(value = null)
    private val _allAppsStateFlow = MutableStateFlow<LoadingState<List<AppWithIcon>>>(value = LoadingState.Loading)

    private val defaultIconPackState = IconPackState(
        allApps = _allAppsStateFlow.value,
        iconPacks = emptyList(),
        iconPackType = _iconPackType.value,
        canSave = false
    )

    init {
        iconPackManager.fetchInstalledIconPacks()
        generalSettingsRepo.iconPackTypeFlow
            .onEach { _iconPackType.value = it }
            .launchIn(scope = viewModelScope)

        _iconPackType
            .mapLatest(::updateAllAppsWithNewIcons)
            .flowOn(context = appCoroutineDispatcher.io)
            .launchIn(scope = viewModelScope)
    }

    private val iconPackApps: Flow<List<App>> = appDrawerRepo.allAppsFlow
        .combine(flow = iconPackManager.iconPacksFlow) { allApps, iconPacks ->
            allApps.filter { app ->
                iconPacks.any { it.packageName == app.packageName }
            }
        }

    val iconPackState = flowOf(value = defaultIconPackState)
        .combine(flow = _allAppsStateFlow) { state, allAppsState ->
            state.copy(
                allApps = allAppsState,
                canSave = allAppsState is LoadingState.Loaded
            )
        }.combine(flow = iconPackApps) { state, iconPackApps ->
            val iconPackType = generalSettingsRepo.iconPackTypeFlow.first()
            state.copy(iconPacks = with(iconProvider) { iconPackApps.toAppWithIcons(iconPackType) })
        }.combine(flow = _iconPackType) { state, iconPackType ->
            state.copy(iconPackType = iconPackType)
        }.withinScope(initialValue = defaultIconPackState)

    private suspend fun updateAllAppsWithNewIcons(iconPackType: IconPackType?) {
        iconPackType ?: return
        _allAppsStateFlow.value = LoadingState.Loading
        iconPackManager.loadIconPack(iconPackType = iconPackType)
        val allApps = appDrawerRepo.allAppsFlow.first()
        val allAppsWithIcon = with(iconProvider) { allApps.toAppWithIcons(iconPackType = iconPackType) }
        _allAppsStateFlow.value = LoadingState.Loaded(value = allAppsWithIcon)
    }

    fun updateSelectedIconPackApp(iconPackType: IconPackType) {
        _iconPackType.value = iconPackType
    }

    fun saveIconPackType() {
        appCoroutineDispatcher.launchInIO {
            val iconPackType = _iconPackType.value ?: return@launchInIO
            generalSettingsRepo.updateIconPackType(iconPackType = iconPackType)
        }
    }
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
