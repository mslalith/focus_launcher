package dev.mslalith.focuslauncher.screens.iconpack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.LoadingState
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.domain.appswithicons.GetAllAppsWithIconsGivenIconPackTypeUseCase
import dev.mslalith.focuslauncher.core.domain.appswithicons.GetIconPackAppsWithIconsUseCase
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import dev.mslalith.focuslauncher.core.model.AppWithIcon
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.screens.iconpack.model.IconPackState
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
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class IconPackViewModel @Inject constructor(
    private val iconPackManager: IconPackManager,
    private val getAllAppsWithIconsGivenIconPackTypeUseCase: GetAllAppsWithIconsGivenIconPackTypeUseCase,
    getIconPackAppsWithIconsUseCase: GetIconPackAppsWithIconsUseCase,
    private val generalSettingsRepo: GeneralSettingsRepo,
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

    private val iconPackAppsWithIcons: Flow<List<AppWithIcon>> = getIconPackAppsWithIconsUseCase()
        .flowOn(context = appCoroutineDispatcher.io)

    val iconPackState = flowOf(value = defaultIconPackState)
        .combine(flow = _allAppsStateFlow) { state, allAppsState ->
            state.copy(
                allApps = allAppsState,
                canSave = allAppsState is LoadingState.Loaded
            )
        }.combine(flow = iconPackAppsWithIcons) { state, iconPackApps ->
            state.copy(iconPacks = iconPackApps)
        }.combine(flow = _iconPackType) { state, iconPackType ->
            state.copy(iconPackType = iconPackType)
        }.withinScope(initialValue = defaultIconPackState)

    private suspend fun updateAllAppsWithNewIcons(iconPackType: IconPackType?) {
        iconPackType ?: return
        _allAppsStateFlow.value = LoadingState.Loading
        iconPackManager.loadIconPack(iconPackType = iconPackType)
        _allAppsStateFlow.value = LoadingState.Loaded(value = getAllAppsWithIconsGivenIconPackTypeUseCase(iconPackType = iconPackType).first())
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
