package dev.mslalith.focuslauncher.feature.favorites

import android.content.ComponentName
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.domain.apps.GetFavoriteColoredAppsUseCase
import dev.mslalith.focuslauncher.core.domain.launcherapps.GetDefaultFavoriteAppsUseCase
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.test.TestLauncherAppsManager
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import dev.mslalith.focuslauncher.core.model.app.AppWithComponent
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import dev.mslalith.focuslauncher.core.testing.toPackageNamed
import kotlinx.collections.immutable.persistentListOf
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
class FavoritesListUiComponentPresenterTest : PresenterTest<FavoritesListUiComponentPresenter, FavoritesListUiComponentState>() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getDefaultFavoriteAppsUseCase: GetDefaultFavoriteAppsUseCase

    @Inject
    lateinit var getFavoriteColoredAppsUseCase: GetFavoriteColoredAppsUseCase

    @Inject
    lateinit var launcherAppsManager: LauncherAppsManager

    @Inject
    lateinit var generalSettingsRepo: GeneralSettingsRepo

    @Inject
    lateinit var favoritesRepo: FavoritesRepo

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    @Inject
    lateinit var appCoroutineDispatcher: AppCoroutineDispatcher

    private val allApps by lazy { TestApps.all.toPackageNamed() }

    @Before
    fun setUp() {
        hiltRule.inject()
        assertThat(launcherAppsManager).isInstanceOf(TestLauncherAppsManager::class.java)
        runBlocking { appDrawerRepo.addApps(apps = allApps) }
    }

    override fun presenterUnderTest() = FavoritesListUiComponentPresenter(
        getDefaultFavoriteAppsUseCase = getDefaultFavoriteAppsUseCase,
        getFavoriteColoredAppsUseCase = getFavoriteColoredAppsUseCase,
        generalSettingsRepo = generalSettingsRepo,
        favoritesRepo = favoritesRepo,
        appCoroutineDispatcher = appCoroutineDispatcher
    )

    @Test
    fun `01 - when apps are loaded and favorites are added, we should get the default favorites back`() = runPresenterTest {
        val defaultApps = listOf(TestApps.Youtube).toPackageNamed()
        assertThat(awaitItem().favoritesList).isEmpty()

        appDrawerRepo.addApps(apps = allApps)
        favoritesRepo.addToFavorites(apps = defaultApps)

        assertThat(awaitItem().favoritesList.toApps()).isEqualTo(defaultApps)
    }

    @Test
    fun `02 - when apps are not loaded and favorites are added, we should get the default favorites back`() = runPresenterTest {
        val defaultApps = listOf(TestApps.Youtube).toPackageNamed()
        favoritesRepo.addToFavorites(apps = defaultApps)

        // since apps are not there yet
        assertThat(awaitItem().favoritesList).isEmpty()

        appDrawerRepo.addApps(apps = allApps)

        assertThat(awaitItem().favoritesList.toApps()).isEqualTo(defaultApps)
    }

    @Test
    fun `03 - when favorites apps are empty, default apps should be added`() = runPresenterTest {
        val app = TestApps.Chrome.toPackageNamed()
        val state = awaitItem()
        assertThat(state.favoritesList).isEmpty()

        launcherAppsManager.test.setFavoritesApps(apps = listOf(app.toAppWithComponent()))
        state.eventSink(FavoritesListUiComponentUiEvent.AddDefaultAppsIfRequired)
        assertFor(expected = persistentListOf(app)) { it.favoritesList.toApps() }
    }
}

private val LauncherAppsManager.test get() = this as TestLauncherAppsManager
private fun List<AppWithColor>.toApps(): List<App> = map { it.app }

private fun App.toAppWithComponent() = AppWithComponent(
    app = this,
    componentName = ComponentName("", "")
)
