package dev.mslalith.focuslauncher.feature.favorites.bottomsheet

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.domain.apps.GetFavoriteColoredAppsUseCase
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.test.TestLauncherAppsManager
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
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
class FavoritesBottomSheetPresenterTest : PresenterTest<FavoritesBottomSheetPresenter, FavoritesBottomSheetState>() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var launcherAppsManager: LauncherAppsManager

    @Inject
    lateinit var favoritesRepo: FavoritesRepo

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    @Inject
    lateinit var getFavoriteColoredAppsUseCase: GetFavoriteColoredAppsUseCase

    @Inject
    lateinit var appCoroutineDispatcher: AppCoroutineDispatcher

    private val allApps by lazy { TestApps.all.toPackageNamed() }

    @Before
    fun setUp() {
        hiltRule.inject()
        assertThat(launcherAppsManager).isInstanceOf(TestLauncherAppsManager::class.java)
        runBlocking { appDrawerRepo.addApps(apps = allApps) }
    }

    override fun presenterUnderTest()  = FavoritesBottomSheetPresenter(
        navigator = navigator,
        favoritesRepo = favoritesRepo,
        getFavoriteColoredAppsUseCase = getFavoriteColoredAppsUseCase,
        appCoroutineDispatcher = appCoroutineDispatcher
    )

    @Test
    fun `01 - when favorite apps are queried, they must be returned`() = runPresenterTest {
        val defaultApps = listOf(TestApps.Youtube).toPackageNamed()
        assertThat(awaitItem().favoritesList).isEmpty()

        appDrawerRepo.addApps(apps = allApps)
        favoritesRepo.addToFavorites(apps = defaultApps)

        assertThat(awaitItem().favoritesList.toApps()).isEqualTo(defaultApps)
    }

    @Test
    fun `02 - when favorites apps are changed, state should be updated`() = runPresenterTest {
        val app = TestApps.Chrome.toPackageNamed()
        val state = awaitItem()
        assertThat(state.favoritesList).isEmpty()

        favoritesRepo.addToFavorites(app = app)
        assertFor(expected = persistentListOf(app)) { it.favoritesList.toApps() }

        state.eventSink(FavoritesBottomSheetUiEvent.Remove(appWithColor = app.toAppWithColor()))
        assertFor(expected = persistentListOf()) { it.favoritesList }
    }

    @Test
    fun `03 - when favorites apps reordered, state should be updated`() = runPresenterTest {
        val chrome = TestApps.Chrome.toPackageNamed()
        val youtube = TestApps.Youtube.toPackageNamed()

        val state = awaitItem()
        assertThat(state.favoritesList).isEmpty()

        favoritesRepo.addToFavorites(listOf(chrome, youtube))
        assertFor(expected = persistentListOf(chrome, youtube)) { it.favoritesList.toApps() }

        state.eventSink(FavoritesBottomSheetUiEvent.Move(fromIndex = 1, toIndex = 0))
        assertFor(expected = persistentListOf(youtube, chrome)) { it.favoritesList.toApps() }
    }

    @Test
    fun `04 - when same favorite app is being reordered, state should not change`() = runPresenterTest {
        val chrome = TestApps.Chrome.toPackageNamed()
        val youtube = TestApps.Youtube.toPackageNamed()

        val state = awaitItem()
        assertThat(state.favoritesList).isEmpty()

        favoritesRepo.addToFavorites(listOf(chrome, youtube))
        assertFor(expected = persistentListOf(chrome, youtube)) { it.favoritesList.toApps() }

        state.eventSink(FavoritesBottomSheetUiEvent.Move(fromIndex = 1, toIndex = 1))
        expectNoEvents()
    }
}

private fun List<AppWithColor>.toApps(): List<App> = map { it.app }

private fun App.toAppWithColor() = AppWithColor(
    app = this,
    color = null
)
