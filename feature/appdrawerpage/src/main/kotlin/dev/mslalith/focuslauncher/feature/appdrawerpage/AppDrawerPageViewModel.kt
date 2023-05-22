package dev.mslalith.focuslauncher.feature.appdrawerpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.model.LoadingState
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.AppDrawerSettingsRepo
import dev.mslalith.focuslauncher.core.domain.apps.GetAppDrawerIconicAppsUseCase
import dev.mslalith.focuslauncher.core.domain.iconpack.ReloadIconPackUseCase
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_VIEW_TYPE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_ICONS
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithIconFavorite
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.feature.appdrawerpage.model.AppDrawerPageState
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
internal class AppDrawerPageViewModel @Inject constructor(
    getAppDrawerIconicAppsUseCase: GetAppDrawerIconicAppsUseCase,
    appDrawerSettingsRepo: AppDrawerSettingsRepo,
    private val reloadIconPackUseCase: ReloadIconPackUseCase,
    private val appDrawerRepo: AppDrawerRepo,
    private val hiddenAppsRepo: HiddenAppsRepo,
    private val favoritesRepo: FavoritesRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    private val searchBarQueryStateFlow = MutableStateFlow(value = "")

    init {
        appDrawerSettingsRepo.searchBarVisibilityFlow
            .onEach { searchAppQuery(query = "") }
            .flowOn(context = appCoroutineDispatcher.io)
            .launchIn(scope = viewModelScope)
    }

    private val defaultAppDrawerPageState = AppDrawerPageState(
        allAppsState = LoadingState.Loading,
        appDrawerViewType = DEFAULT_APP_DRAWER_VIEW_TYPE,
        showAppIcons = DEFAULT_APP_ICONS,
        showAppGroupHeader = DEFAULT_APP_GROUP_HEADER,
        showSearchBar = DEFAULT_SEARCH_BAR,
        searchBarQuery = searchBarQueryStateFlow.value
    )

    private val allAppsWithIcons: Flow<List<AppWithIconFavorite>> = getAppDrawerIconicAppsUseCase(
        searchQueryFlow = searchBarQueryStateFlow
    ).flowOn(context = appCoroutineDispatcher.io)

    val appDrawerPageState = flowOf(value = defaultAppDrawerPageState)
        .combine(flow = appDrawerSettingsRepo.appDrawerViewTypeFlow) { state, appDrawerViewType ->
            state.copy(appDrawerViewType = appDrawerViewType)
        }.combine(flow = appDrawerSettingsRepo.appIconsVisibilityFlow) { state, showAppIcons ->
            state.copy(showAppIcons = showAppIcons)
        }.combine(flow = appDrawerSettingsRepo.appGroupHeaderVisibilityFlow) { state, showAppGroupHeader ->
            state.copy(showAppGroupHeader = showAppGroupHeader)
        }.combine(flow = appDrawerSettingsRepo.searchBarVisibilityFlow) { state, showSearchBar ->
            state.copy(showSearchBar = showSearchBar)
        }.combine(flow = searchBarQueryStateFlow) { state, searchBarQuery ->
            state.copy(searchBarQuery = searchBarQuery)
        }.combine(flow = allAppsWithIcons) { state, apps ->
            state.copy(allAppsState = LoadingState.Loaded(value = apps.toImmutableList()))
        }.withinScope(initialValue = defaultAppDrawerPageState)

    fun searchAppQuery(query: String) {
        searchBarQueryStateFlow.value = query
    }

    fun reloadIconPack() = reloadIconPackUseCase()

    fun updateDisplayName(app: App, displayName: String) {
        appCoroutineDispatcher.launchInIO {
            appDrawerRepo.updateDisplayName(app = app, displayName = displayName)
        }
    }

    fun addToFavorites(app: App) {
        appCoroutineDispatcher.launchInIO {
            favoritesRepo.addToFavorites(app = app)
        }
    }

    fun removeFromFavorites(app: App) {
        appCoroutineDispatcher.launchInIO {
            favoritesRepo.removeFromFavorites(packageName = app.packageName)
        }
    }

    fun addToHiddenApps(app: App) {
        appCoroutineDispatcher.launchInIO {
            hiddenAppsRepo.addToHiddenApps(app = app)
        }
    }
}
