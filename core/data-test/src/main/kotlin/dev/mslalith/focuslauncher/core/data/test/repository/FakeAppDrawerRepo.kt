package dev.mslalith.focuslauncher.core.data.test.repository

import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.testing.TestApps
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking

class FakeAppDrawerRepo : AppDrawerRepo {

    private val allAppsStateFlow = MutableStateFlow(value = emptyList<App>())
    override val allAppsFlow: Flow<List<App>> = allAppsStateFlow

    override suspend fun getAppBy(packageName: String): App? = allAppsStateFlow.value.firstOrNull {
        it.packageName == packageName
    }

    override suspend fun addApps(apps: List<App>) = allAppsStateFlow.update { it + apps }

    override suspend fun addApp(app: App) = allAppsStateFlow.update { it + app }

    override suspend fun removeApp(app: App) = allAppsStateFlow.update { it - app }

    override suspend fun clearApps() = allAppsStateFlow.update { emptyList() }

    override suspend fun updateDisplayName(app: App, displayName: String) = allAppsStateFlow.update { apps ->
        apps.map {
            if (it.packageName == app.packageName) it.copy(displayName = displayName)
            else it
        }
    }

    override suspend fun areAppsEmptyInDatabase(): Boolean = allAppsStateFlow.value.isEmpty()

    fun addTestApps(apps: List<App> = TestApps.all) = runBlocking { addApps(apps) }
}
