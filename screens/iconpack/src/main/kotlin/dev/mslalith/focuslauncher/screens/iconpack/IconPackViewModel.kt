package dev.mslalith.focuslauncher.screens.iconpack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.LoadingState
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.domain.apps.GetAllAppsOnIconPackChangeUseCase
import dev.mslalith.focuslauncher.core.domain.apps.GetIconPackIconicAppsUseCase
import dev.mslalith.focuslauncher.core.domain.iconpack.FetchIconPacksUseCase
import dev.mslalith.focuslauncher.core.domain.iconpack.LoadIconPackUseCase
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.AppWithIconFavorite
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.screens.iconpack.model.IconPackState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
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
    private val getAllAppsOnIconPackChangeUseCase: GetAllAppsOnIconPackChangeUseCase,
    getIconPackIconicAppsUseCase: GetIconPackIconicAppsUseCase,
    fetchIconPacksUseCase: FetchIconPacksUseCase,
    private val loadIconPackUseCase: LoadIconPackUseCase,
    private val generalSettingsRepo: GeneralSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    private val _iconPackType = MutableStateFlow<IconPackType?>(value = null)
    private val _allAppsStateFlow = MutableStateFlow<LoadingState<ImmutableList<AppWithIconFavorite>>>(value = LoadingState.Loading)

    private val defaultIconPackState = IconPackState(
        allApps = _allAppsStateFlow.value,
        iconPacks = persistentListOf(),
        iconPackType = _iconPackType.value,
        canSave = false
    )

    init {
        fetchIconPacksUseCase()
        generalSettingsRepo.iconPackTypeFlow
            .onEach { _iconPackType.value = it }
            .launchIn(scope = viewModelScope)

        _iconPackType
            .mapLatest(::updateAllAppsWithNewIcons)
            .flowOn(context = appCoroutineDispatcher.io)
            .launchIn(scope = viewModelScope)
    }

    private val iconPackAppsWithIcons: Flow<List<AppWithIcon>> = getIconPackIconicAppsUseCase()
        .flowOn(context = appCoroutineDispatcher.io)

    val iconPackState = flowOf(value = defaultIconPackState)
        .combine(flow = _allAppsStateFlow) { state, allAppsState ->
            state.copy(
                allApps = allAppsState,
                canSave = allAppsState is LoadingState.Loaded
            )
        }.combine(flow = iconPackAppsWithIcons) { state, iconPackApps ->
            state.copy(iconPacks = iconPackApps.toImmutableList())
        }.combine(flow = _iconPackType) { state, iconPackType ->
            state.copy(iconPackType = iconPackType)
        }.withinScope(initialValue = defaultIconPackState)

    private suspend fun updateAllAppsWithNewIcons(iconPackType: IconPackType?) {
        iconPackType ?: return
        _allAppsStateFlow.value = LoadingState.Loading
        loadIconPackUseCase(iconPackType = iconPackType)
        _allAppsStateFlow.value = LoadingState.Loaded(value = getAllAppsOnIconPackChangeUseCase(iconPackType = iconPackType).first().toImmutableList())
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
