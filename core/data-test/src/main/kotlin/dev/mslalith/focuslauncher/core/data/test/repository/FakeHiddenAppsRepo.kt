package dev.mslalith.focuslauncher.core.data.test.repository

import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.app.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeHiddenAppsRepo(
    fakeAppDrawerRepo: FakeAppDrawerRepo
) : HiddenAppsRepo {

    private val onlyHiddenAppsStateFlow = MutableStateFlow(value = emptyList<App>())

    override val onlyHiddenAppsFlow: Flow<List<App>> = onlyHiddenAppsStateFlow
        .map { hiddenApps ->
            hiddenApps.mapNotNull {
                fakeAppDrawerRepo.getAppBy(packageName = it.packageName)
            }
        }

    override suspend fun addToHiddenApps(app: App) {
        onlyHiddenAppsStateFlow.update { it + app }
    }

    override suspend fun addToHiddenApps(apps: List<App>) {
        onlyHiddenAppsStateFlow.update { it + apps }
    }

    override suspend fun removeFromHiddenApps(packageName: String) = onlyHiddenAppsStateFlow.update { apps ->
        apps.filter { it.packageName != packageName }
    }

    override suspend fun clearHiddenApps() {
        onlyHiddenAppsStateFlow.update { emptyList() }
    }

    override suspend fun isHidden(packageName: String): Boolean = onlyHiddenAppsStateFlow.value
        .firstOrNull { it.packageName == packageName } != null
}
