package dev.mslalith.focuslauncher.feature.appdrawerpage

import app.cash.turbine.ReceiveTurbine
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.model.LoadingState
import dev.mslalith.focuslauncher.core.common.model.getOrNull
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.AppDrawerSettingsRepo
import dev.mslalith.focuslauncher.core.domain.apps.GetAppDrawerIconicAppsUseCase
import dev.mslalith.focuslauncher.core.domain.iconpack.ReloadIconPackAfterFirstLoadUseCase
import dev.mslalith.focuslauncher.core.domain.iconpack.ReloadIconPackUseCase
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.test.TestLauncherAppsManager
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import dev.mslalith.focuslauncher.core.testing.disableAsSystem
import dev.mslalith.focuslauncher.core.testing.toPackageNamed
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AppRobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class AppDrawerPagePresenterTest : PresenterTest<AppDrawerPagePresenter, AppDrawerPageState>() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getAppDrawerIconicAppsUseCase: GetAppDrawerIconicAppsUseCase

    @Inject
    lateinit var reloadIconPackAfterFirstLoadUseCase: ReloadIconPackAfterFirstLoadUseCase

    @Inject
    lateinit var appDrawerSettingsRepo: AppDrawerSettingsRepo

    @Inject
    lateinit var reloadIconPackUseCase: ReloadIconPackUseCase

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    @Inject
    lateinit var launcherAppsManager: LauncherAppsManager

    @Inject
    lateinit var appCoroutineDispatcher: AppCoroutineDispatcher

    private val allApps by lazy { TestApps.all.toPackageNamed().disableAsSystem() }

    @Before
    fun setUp() {
        hiltRule.inject()
        assertThat(launcherAppsManager).isInstanceOf(TestLauncherAppsManager::class.java)
        runBlocking {
            appDrawerRepo.addApps(apps = allApps)
        }
    }

    override fun presenterUnderTest() = AppDrawerPagePresenter(
        getAppDrawerIconicAppsUseCase = getAppDrawerIconicAppsUseCase,
        reloadIconPackAfterFirstLoadUseCase = reloadIconPackAfterFirstLoadUseCase,
        appDrawerSettingsRepo = appDrawerSettingsRepo,
        reloadIconPackUseCase = reloadIconPackUseCase,
        appCoroutineDispatcher = appCoroutineDispatcher
    )

    @Test
    fun `01 - when search input is given, apps should be filtered`() = runPresenterTest {
        val chromeApp = allApps.first { it.packageName.contains("chrome") }

        val state = awaitItem()
        assertDrawerApps(expected = allApps)

        state.eventSink(AppDrawerPageUiEvent.UpdateSearchQuery(query = chromeApp.name))
        assertDrawerApps(expected = listOf(chromeApp))
    }

    @Test
    fun `02 - when search input is cleared, all apps must be shown`() = runPresenterTest {
        val chromeApp = allApps.first { it.packageName.contains("chrome") }

        val state = awaitItem()
        assertDrawerApps(expected = allApps)

        state.eventSink(AppDrawerPageUiEvent.UpdateSearchQuery(query = chromeApp.name))
        assertDrawerApps(expected = listOf(chromeApp))

        state.eventSink(AppDrawerPageUiEvent.UpdateSearchQuery(query = ""))
        assertDrawerApps(expected = allApps)
    }

    @Test
    fun `03 - when search input is given & search is disabled, all apps must be shown`() = runPresenterTest {
        val chromeApp = allApps.first { it.packageName.contains("chrome") }

        val state = awaitItem()
        assertDrawerApps(expected = allApps)

        state.eventSink(AppDrawerPageUiEvent.UpdateSearchQuery(query = chromeApp.name))
        assertDrawerApps(expected = listOf(chromeApp))

        appDrawerSettingsRepo.toggleSearchBarVisibility()
        assertDrawerApps(expected = allApps)
    }

    @Test
    fun `04 - when user searches, query should be updated`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.searchBarQuery).isEmpty()

        val query = "chrome"
        state.eventSink(AppDrawerPageUiEvent.UpdateSearchQuery(query = query))
        assertFor(expected = query) { it.searchBarQuery }

        cancelAndIgnoreRemainingEvents()
    }

    @Test
    fun `04 - when app is updated and name has changed with no display name set, new name should be used`() = runPresenterTest {
        // Twitter -> X
        val chromeApp = allApps.first { it.packageName.contains("chrome") }

        assertFor(expected = allApps) { it.allAppsState.toTestApps() }

        val newChromeApp = chromeApp.copy(name = "New Chrome")
        launcherAppsManager.test.loadAppForPackage(packageName = chromeApp.packageName) {
            it.copy(app = it.app.copy(name = newChromeApp.name))
        }

        // update Chrome app
        appDrawerRepo.removeApp(chromeApp)
        appDrawerRepo.addApp(newChromeApp)

        val newAllApps = allApps.map { if (it.packageName == newChromeApp.packageName) newChromeApp else it }
        assertFor(expected = newAllApps) { it.allAppsState.toTestApps() }
    }

    @Test
    fun `04 - when app is updated and name has changed with display name set, display name should be used`() = runPresenterTest {
        // Twitter -> X
        val chromeApp = allApps.first { it.packageName.contains("chrome") }
        val chromeAppWithDisplayName = chromeApp.copy(displayName = "Chrome display name")

        launcherAppsManager.test.loadAppForPackage(packageName = chromeApp.packageName) {
            it.copy(app = it.app.copy(displayName = chromeAppWithDisplayName.displayName))
        }

        appDrawerRepo.removeApp(chromeApp)
        appDrawerRepo.addApp(chromeAppWithDisplayName)

        // verify existing app
        var newAllApps = allApps.map { if (it.packageName == chromeAppWithDisplayName.packageName) chromeAppWithDisplayName else it }
        assertFor(expected = newAllApps) { it.allAppsState.toTestApps() }

        val newChromeApp = chromeAppWithDisplayName.copy(name = "New Chrome")
        launcherAppsManager.test.loadAppForPackage(packageName = chromeApp.packageName) {
            // here we update name and keep previous displayName as user updated
            it.copy(app = it.app.copy(name = newChromeApp.name, displayName = newChromeApp.displayName))
        }

        // update Chrome app
        appDrawerRepo.removeApp(chromeApp)
        appDrawerRepo.addApp(newChromeApp)

        // verify updated app
        newAllApps = allApps.map { if (it.packageName == newChromeApp.packageName) newChromeApp else it }
        assertFor(expected = newAllApps) { it.allAppsState.toTestApps() }
    }

    context (ReceiveTurbine<AppDrawerPageState>)
    private suspend fun assertDrawerApps(
        expected: List<App>
    ) = assertFor(expected = expected) { it.allAppsState.toTestApps() }
}

private val LauncherAppsManager.test: TestLauncherAppsManager get() = this as TestLauncherAppsManager
private fun LoadingState<ImmutableList<AppDrawerItem>>.toTestApps(): List<App>? = getOrNull()?.map { it.app }
