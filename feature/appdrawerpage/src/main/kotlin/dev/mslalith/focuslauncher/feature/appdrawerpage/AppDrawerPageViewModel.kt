package dev.mslalith.focuslauncher.feature.appdrawerpage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.AppDrawerSettingsRepo
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_VIEW_TYPE
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_ICONS
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.IconProvider
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon
import dev.mslalith.focuslauncher.feature.appdrawerpage.model.AppDrawerPageState
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@HiltViewModel
internal class AppDrawerPageViewModel @Inject constructor(
    private val iconProvider: IconProvider,
    appDrawerSettingsRepo: AppDrawerSettingsRepo,
    private val appDrawerRepo: AppDrawerRepo,
    private val hiddenAppsRepo: HiddenAppsRepo,
    private val favoritesRepo: FavoritesRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    private val searchBarQueryStateFlow = MutableStateFlow("")

    private val defaultAppDrawerPageState = AppDrawerPageState(
        allApps = emptyList(),
        appDrawerViewType = DEFAULT_APP_DRAWER_VIEW_TYPE,
        showAppIcons = DEFAULT_APP_ICONS,
        showAppGroupHeader = DEFAULT_APP_GROUP_HEADER,
        showSearchBar = DEFAULT_SEARCH_BAR,
        searchBarQuery = searchBarQueryStateFlow.value
    )

    private val appDrawerAppsFlow: Flow<List<App>> = appDrawerRepo.allAppsFlow
        .combine(hiddenAppsRepo.onlyHiddenAppsFlow) { allApps, hiddenApps ->
            allApps - hiddenApps.toSet()
        }.combine(searchBarQueryStateFlow) { filteredApps, query ->
            when {
                query.isNotEmpty() -> filteredApps.filter {
                    it.name.startsWith(
                        prefix = query,
                        ignoreCase = true
                    )
                }
                else -> filteredApps
            }
        }

    private val appsWithIconFlow: Flow<List<AppWithIcon>> = appDrawerAppsFlow.map { apps ->
        apps.map { app ->
            AppWithIcon(
                name = app.name,
                displayName = app.displayName,
                packageName = app.packageName,
                icon = iconProvider.iconFor(app.packageName),
                isSystem = app.isSystem
            )
        }
    }

    val appDrawerPageState = flowOf(defaultAppDrawerPageState)
        .combine(appDrawerSettingsRepo.appDrawerViewTypeFlow) { state, appDrawerViewType ->
            state.copy(appDrawerViewType = appDrawerViewType)
        }.combine(appDrawerSettingsRepo.appIconsVisibilityFlow) { state, showAppIcons ->
            state.copy(showAppIcons = showAppIcons)
        }.combine(appDrawerSettingsRepo.appGroupHeaderVisibilityFlow) { state, showAppGroupHeader ->
            state.copy(showAppGroupHeader = showAppGroupHeader)
        }.combine(appDrawerSettingsRepo.searchBarVisibilityFlow) { state, showSearchBar ->
            state.copy(showSearchBar = showSearchBar)
        }.combine(searchBarQueryStateFlow) { state, searchBarQuery ->
            state.copy(searchBarQuery = searchBarQuery)
        }.combine(appsWithIconFlow) { state, apps ->
            state.copy(allApps = apps)
        }.withinScope(initialValue = defaultAppDrawerPageState)

    fun searchAppQuery(query: String) {
        searchBarQueryStateFlow.value = query
    }

    fun updateDisplayName(app: App, displayName: String) {
        appCoroutineDispatcher.launchInIO {
            appDrawerRepo.updateDisplayName(app, displayName)
        }
    }

    suspend fun isFavorite(packageName: String) = favoritesRepo.isFavorite(packageName)

    fun addToFavorites(app: App) {
        appCoroutineDispatcher.launchInIO {
            favoritesRepo.addToFavorites(app)
        }
    }

    fun removeFromFavorites(app: App) {
        appCoroutineDispatcher.launchInIO {
            favoritesRepo.removeFromFavorites(app.packageName)
        }
    }

    fun addToHiddenApps(app: App) {
        appCoroutineDispatcher.launchInIO {
            hiddenAppsRepo.addToHiddenApps(app)
        }
    }
}
