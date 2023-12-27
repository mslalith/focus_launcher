package dev.mslalith.focuslauncher.feature.appdrawerpage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.model.LoadingState
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.AppDrawerSettingsRepo
import dev.mslalith.focuslauncher.core.domain.apps.GetAppDrawerIconicAppsUseCase
import dev.mslalith.focuslauncher.core.domain.iconpack.ReloadIconPackAfterFirstLoadUseCase
import dev.mslalith.focuslauncher.core.domain.iconpack.ReloadIconPackUseCase
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_ICON_VIEW_TYPE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_VIEW_TYPE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import dev.mslalith.focuslauncher.core.screens.AppDrawerPageScreen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@CircuitInject(AppDrawerPageScreen::class, SingletonComponent::class)
class AppDrawerPagePresenter @Inject constructor(
    getAppDrawerIconicAppsUseCase: GetAppDrawerIconicAppsUseCase,
    private val reloadIconPackAfterFirstLoadUseCase: ReloadIconPackAfterFirstLoadUseCase,
    private val appDrawerSettingsRepo: AppDrawerSettingsRepo,
    private val reloadIconPackUseCase: ReloadIconPackUseCase,
    private val hiddenAppsRepo: HiddenAppsRepo,
    private val favoritesRepo: FavoritesRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<AppDrawerPageState> {

    private var searchBarQuery by mutableStateOf(value = "")
    private var allAppsState by mutableStateOf<LoadingState<ImmutableList<AppDrawerItem>>>(value = LoadingState.Loading)

    private val allAppDrawerItems: Flow<List<AppDrawerItem>> = getAppDrawerIconicAppsUseCase(
        searchQueryFlow = snapshotFlow { searchBarQuery }
    ).flowOn(context = appCoroutineDispatcher.io)

    @Composable
    override fun present(): AppDrawerPageState {
        val scope = rememberCoroutineScope()

        val appDrawerViewType by appDrawerSettingsRepo.appDrawerViewTypeFlow.collectAsState(initial = DEFAULT_APP_DRAWER_VIEW_TYPE)
        val appDrawerIconViewType by appDrawerSettingsRepo.appDrawerIconViewTypeFlow.collectAsState(initial = DEFAULT_APP_DRAWER_ICON_VIEW_TYPE)
        val showAppGroupHeader by appDrawerSettingsRepo.appGroupHeaderVisibilityFlow.collectAsState(initial = DEFAULT_APP_GROUP_HEADER)
        val showSearchBar by appDrawerSettingsRepo.searchBarVisibilityFlow.collectAsState(initial = DEFAULT_SEARCH_BAR)

        LaunchedEffect(key1 = Unit) { reloadIconPackAfterFirstLoadUseCase() }

        LaunchedEffect(key1 = allAppDrawerItems) {
            allAppDrawerItems.collectLatest {
                allAppsState = LoadingState.Loaded(it.toImmutableList())
            }
        }

        LaunchedEffect(key1 = Unit) {
            appDrawerSettingsRepo.searchBarVisibilityFlow
                .onEach { searchBarQuery = "" }
                .flowOn(context = appCoroutineDispatcher.io)
                .collect()
        }

        return AppDrawerPageState(
            allAppsState = allAppsState,
            appDrawerViewType = appDrawerViewType,
            appDrawerIconViewType = appDrawerIconViewType,
            showAppGroupHeader = showAppGroupHeader,
            showSearchBar = showSearchBar,
            searchBarQuery = searchBarQuery
        ) {
            when (it) {
                is AppDrawerPageUiEvent.UpdateSearchQuery -> searchBarQuery = it.query
                AppDrawerPageUiEvent.ReloadIconPack -> scope.reloadIconPack()
                is AppDrawerPageUiEvent.AddToFavorites -> scope.addToFavorites(app = it.app)
                is AppDrawerPageUiEvent.AddToHiddenApps -> scope.addToHiddenApps(app = it.app)
                is AppDrawerPageUiEvent.RemoveFromFavorites -> scope.removeFromFavorites(app = it.app)
            }
        }
    }

    private fun CoroutineScope.reloadIconPack() {
        launch(appCoroutineDispatcher.io) {
            reloadIconPackUseCase()
        }
    }

    private fun CoroutineScope.addToFavorites(app: App) {
        launch(appCoroutineDispatcher.io) {
            favoritesRepo.addToFavorites(app = app)
        }
    }

    private fun CoroutineScope.removeFromFavorites(app: App) {
        launch(appCoroutineDispatcher.io) {
            favoritesRepo.removeFromFavorites(packageName = app.packageName)
        }
    }

    private fun CoroutineScope.addToHiddenApps(app: App) {
        launch(appCoroutineDispatcher.io) {
            hiddenAppsRepo.addToHiddenApps(app = app)
        }
    }
}
