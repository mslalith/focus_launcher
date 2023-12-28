package dev.mslalith.focuslauncher.core.data.test.repository

import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.model.app.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

class FakeFavoritesRepo(
    fakeAppDrawerRepo: FakeAppDrawerRepo
) : FavoritesRepo {

    private val onlyFavoritesStateFlow = MutableStateFlow(value = emptyList<App>())

    override val onlyFavoritesFlow: Flow<List<App>> = onlyFavoritesStateFlow
        .combine(flow = fakeAppDrawerRepo.allAppsFlow) { onlyFavorites, allApps ->
            onlyFavorites.filter { favorite ->
                allApps.any { it.packageName == favorite.packageName }
            }.mapNotNull {
                fakeAppDrawerRepo.getAppBy(packageName = it.packageName)
            }
        }

    override suspend fun addToFavorites(app: App) {
        onlyFavoritesStateFlow.update { it + app }
    }

    override suspend fun addToFavorites(apps: List<App>) {
        onlyFavoritesStateFlow.update { it + apps }
    }

    override suspend fun reorderFavorite(app: App, withApp: App) {
        val apps = onlyFavoritesStateFlow.value.toMutableList()
        val appIndex = apps.indexOfFirst { it.packageName == app.packageName }
        val withAppIndex = apps.indexOfFirst { it.packageName == withApp.packageName }
        if (appIndex == -1 || withAppIndex == -1) return

        apps[appIndex] = withApp
        apps[withAppIndex] = app

        onlyFavoritesStateFlow.update { apps }
    }

    override suspend fun removeFromFavorites(packageName: String) = onlyFavoritesStateFlow.update { apps ->
        apps.filter { it.packageName != packageName }
    }

    override suspend fun clearFavorites() {
        onlyFavoritesStateFlow.update { emptyList() }
    }

    override suspend fun isFavorite(packageName: String): Boolean = onlyFavoritesStateFlow.value
        .firstOrNull { it.packageName == packageName } != null
}
