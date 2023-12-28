package dev.mslalith.focuslauncher.screens.editfavorites

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.test.TestAppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.test.repository.FakeAppDrawerRepo
import dev.mslalith.focuslauncher.core.data.test.repository.FakeFavoritesRepo
import dev.mslalith.focuslauncher.core.data.test.repository.FakeHiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.SelectedApp
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
class EditFavoritesPresenterTest : PresenterTest<EditFavoritesPresenter, EditFavoritesState>() {

    private val appDrawerRepo = FakeAppDrawerRepo()
    private val favoritesRepo = FakeFavoritesRepo(fakeAppDrawerRepo = appDrawerRepo)
    private val hiddenAppsRepo = FakeHiddenAppsRepo(fakeAppDrawerRepo = appDrawerRepo)
    private val appCoroutineDispatcher = TestAppCoroutineDispatcher()

    @Before
    fun setup() {
        appDrawerRepo.addTestApps()
    }

    override fun presenterUnderTest() = EditFavoritesPresenter(
        navigator = navigator,
        appDrawerRepo = appDrawerRepo,
        favoritesRepo = favoritesRepo,
        hiddenAppsRepo = hiddenAppsRepo,
        appCoroutineDispatcher = appCoroutineDispatcher
    )

    @Test
    fun `01 - initially favorites must not be selected`() = runPresenterTest {
        assertThat(awaitItem().favoriteApps).isEmpty()
        assertThat(awaitItem().favoriteApps).isEqualTo(TestApps.all.toSelectedAppWith(isSelected = false))
    }

    @Test
    fun `02 - when all apps are added to favorite, every item in the list must be selected`() = runPresenterTest {
        val apps = TestApps.all

        val state = awaitItem()
        assertThat(state.favoriteApps).isEmpty()

        apps.forEach { state.eventSink(EditFavoritesUiEvent.AddToFavorites(app = it)) }
        assertFor(expected = apps.toSelectedAppWith(isSelected = true)) { it.favoriteApps }
    }

    @Test
    fun `03 - when all apps are removed from favorites, every item in the list must not be selected`() = runPresenterTest {
        val apps = TestApps.all

        val state = awaitItem()
        assertThat(state.favoriteApps).isEmpty()

        apps.forEach { state.eventSink(EditFavoritesUiEvent.AddToFavorites(app = it)) }
        assertFor(expected = apps.toSelectedAppWith(isSelected = true)) { it.favoriteApps }

        apps.forEach { state.eventSink(EditFavoritesUiEvent.RemoveFromFavorites(app = it)) }
        assertFor(expected = apps.toSelectedAppWith(isSelected = false)) { it.favoriteApps }
    }

    @Test
    fun `04 - when favorites are cleared, every item in the list must not be selected`() = runPresenterTest {
        val apps = TestApps.all

        val state = awaitItem()
        assertThat(state.favoriteApps).isEmpty()

        apps.forEach { state.eventSink(EditFavoritesUiEvent.AddToFavorites(app = it)) }
        assertFor(expected = apps.toSelectedAppWith(isSelected = true)) { it.favoriteApps }

        state.eventSink(EditFavoritesUiEvent.ClearFavorites)
        assertFor(expected = apps.toSelectedAppWith(isSelected = false)) { it.favoriteApps }
    }

    @Test
    fun `05 - when hidden apps are not shown & an app is hidden, it should not be listed in favorites`() = runPresenterTest {
        val hiddenApps = listOf(TestApps.Phone, TestApps.Chrome)
        val apps = TestApps.all - hiddenApps.toSet()

        val state = awaitItem()
        assertThat(state.favoriteApps).isEmpty()

        hiddenAppsRepo.addToHiddenApps(apps = hiddenApps)
        assertFor(expected = apps.toSelectedAppWith(isSelected = false)) { it.favoriteApps }
    }

    @Test
    fun `06 - when hidden apps are shown & an app is hidden, it should be listed in favorites as disabled`() = runPresenterTest {
        val hiddenApps = listOf(TestApps.Phone, TestApps.Chrome)
        val totalApps = TestApps.all
        val apps = totalApps - hiddenApps.toSet()

        val state = awaitItem()
        assertThat(state.favoriteApps).isEmpty()

        hiddenAppsRepo.addToHiddenApps(apps = hiddenApps)

        assertFor(expected = apps.toSelectedAppWith(isSelected = false)) { it.favoriteApps }

        state.eventSink(EditFavoritesUiEvent.ToggleShowingHiddenApps)

        val expected = totalApps.map { it.toSelectedAppWith(isSelected = false, disabled = hiddenApps.contains(it)) }
        assertFor(expected = expected) { it.favoriteApps }
    }
}

private fun App.toSelectedAppWith(
    isSelected: Boolean = false,
    disabled: Boolean = false
): SelectedApp = SelectedApp(app = this, isSelected = isSelected, disabled = disabled)

private fun List<App>.toSelectedAppWith(
    isSelected: Boolean = false,
    disabled: Boolean = false
): List<SelectedApp> = map { it.toSelectedAppWith(isSelected = isSelected, disabled = disabled) }
