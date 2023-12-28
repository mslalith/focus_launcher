package dev.mslalith.focuslauncher.screens.hideapps

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.test.TestAppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.test.repository.FakeAppDrawerRepo
import dev.mslalith.focuslauncher.core.data.test.repository.FakeFavoritesRepo
import dev.mslalith.focuslauncher.core.data.test.repository.FakeHiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.SelectedHiddenApp
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AppRobolectricTestRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class HideAppsPresenterTest : PresenterTest<HideAppsPresenter, HideAppsState>() {

    private val appDrawerRepo = FakeAppDrawerRepo()
    private val favoritesRepo = FakeFavoritesRepo(fakeAppDrawerRepo = appDrawerRepo)
    private val hiddenAppsRepo = FakeHiddenAppsRepo(fakeAppDrawerRepo = appDrawerRepo)
    private val appCoroutineDispatcher = TestAppCoroutineDispatcher()

    @Before
    fun setup() {
        appDrawerRepo.addTestApps()
    }

    override fun presenterUnderTest() = HideAppsPresenter(
        navigator = navigator,
        appDrawerRepo = appDrawerRepo,
        favoritesRepo = favoritesRepo,
        hiddenAppsRepo = hiddenAppsRepo,
        appCoroutineDispatcher = appCoroutineDispatcher
    )

    @Test
    fun `01 - initially hidden apps must not be selected`() = runPresenterTest {
        assertThat(awaitItem().hiddenApps).isEmpty()
        assertThat(awaitItem().hiddenApps).isEqualTo(TestApps.all.toSelectedHiddenAppWith(isSelected = false))
    }

    @Test
    fun `02 - when all apps are hidden, every item in the list must be selected`() = runPresenterTest {
        val apps = TestApps.all

        val state = awaitItem()
        assertThat(state.hiddenApps).isEmpty()

        apps.forEach { state.eventSink(HideAppsUiEvent.AddToHiddenApps(app = it)) }

        assertFor(expected = apps.toSelectedHiddenAppWith(isSelected = true)) { it.hiddenApps }
    }

    @Test
    fun `03 - when a hidden app is un-hidden, it must not be selected`() = runPresenterTest {
        val apps = TestApps.all
        val hiddenApp = TestApps.Chrome

        val state = awaitItem()
        assertThat(state.hiddenApps).isEmpty()

        apps.forEach { state.eventSink(HideAppsUiEvent.AddToHiddenApps(app = it)) }
        assertFor(expected = apps.toSelectedHiddenAppWith(isSelected = true)) { it.hiddenApps }

        state.eventSink(HideAppsUiEvent.RemoveFromHiddenApps(app = hiddenApp))

        val appsWithoutHidden = apps.map { it.toSelectedHiddenAppWith(isSelected = it != hiddenApp) }
        assertThat(awaitItem().hiddenApps).isEqualTo(appsWithoutHidden)
    }

    @Test
    fun `04 - when hidden apps are cleared, every item in the list must not be selected`() = runPresenterTest {
        val apps = TestApps.all

        val state = awaitItem()
        assertThat(state.hiddenApps).isEmpty()

        apps.forEach { state.eventSink(HideAppsUiEvent.AddToHiddenApps(app = it)) }
        assertFor(expected = apps.toSelectedHiddenAppWith(isSelected = true)) { it.hiddenApps }

        state.eventSink(HideAppsUiEvent.ClearHiddenApps)
        assertThat(awaitItem().hiddenApps).isEqualTo(apps.toSelectedHiddenAppWith(isSelected = false))
    }

    @Test
    fun `05 - when a favorite app is being hidden, it must be removed from favorites list`() = runPresenterTest {
        val allApps = TestApps.all
        val favoriteApp = TestApps.Phone

        val state = awaitItem()
        assertThat(state.hiddenApps).isEmpty()

        favoritesRepo.addToFavorites(app = favoriteApp)

        var expected = allApps.map { it.toSelectedHiddenAppWith(isSelected = false, isFavorite = it == favoriteApp) }
        assertFor(expected = expected) { it.hiddenApps }

        hiddenAppsRepo.addToHiddenApps(app = favoriteApp)

        expected = allApps.map { it.toSelectedHiddenAppWith(isSelected = it == favoriteApp, isFavorite = it == favoriteApp) }
        assertThat(awaitItem().hiddenApps).isEqualTo(expected)

        favoritesRepo.removeFromFavorites(packageName = favoriteApp.packageName)

        expected = allApps.map { it.toSelectedHiddenAppWith(isSelected = it == favoriteApp, isFavorite = false) }
        assertThat(awaitItem().hiddenApps).isEqualTo(expected)
    }
}

private fun App.toSelectedHiddenAppWith(
    isSelected: Boolean = false,
    isFavorite: Boolean = false
): SelectedHiddenApp = SelectedHiddenApp(app = this, isSelected = isSelected, isFavorite = isFavorite)

private fun List<App>.toSelectedHiddenAppWith(
    isSelected: Boolean = false,
    isFavorite: Boolean = false
): List<SelectedHiddenApp> = map { it.toSelectedHiddenAppWith(isSelected = isSelected, isFavorite = isFavorite) }
