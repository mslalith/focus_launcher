package dev.mslalith.focuslauncher.screens.hideapps

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.SelectedHiddenApp
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class HideAppsViewModelTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    @Inject
    lateinit var favoritesRepo: FavoritesRepo

    @Inject
    lateinit var hiddenAppsRepo: HiddenAppsRepo

    @Inject
    lateinit var appCoroutineDispatcher: AppCoroutineDispatcher

    private lateinit var viewModel: HideAppsViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = HideAppsViewModel(
            appDrawerRepo = appDrawerRepo,
            favoritesRepo = favoritesRepo,
            hiddenAppsRepo = hiddenAppsRepo,
            appCoroutineDispatcher = appCoroutineDispatcher
        )
        runBlocking {
            appDrawerRepo.addApps(apps = TestApps.all)
        }
    }

    @Test
    fun `01 - initially hidden apps must not be selected`() = runCoroutineTest {
        assertThat(viewModel.hideAppsState.value.hiddenApps).isEmpty()
        assertThat(viewModel.hideAppsState.awaitItem().hiddenApps).isEqualTo(TestApps.all.toSelectedHiddenAppWith(isSelected = false))
    }

    @Test
    fun `02 - when all apps are hidden, every item in the list must be selected`() = runCoroutineTest {
        val apps = TestApps.all

        apps.forEach { viewModel.addToHiddenApps(app = it) }
        assertThat(viewModel.hideAppsState.awaitItem().hiddenApps).isEqualTo(apps.toSelectedHiddenAppWith(isSelected = true))
    }

    @Test
    fun `03 - when a hidden app is un-hidden, it must not be selected`() = runCoroutineTest {
        val apps = TestApps.all
        val hiddenApp = TestApps.Chrome

        apps.forEach { viewModel.addToHiddenApps(app = it) }
        assertThat(viewModel.hideAppsState.awaitItem().hiddenApps).isEqualTo(apps.toSelectedHiddenAppWith(isSelected = true))

        viewModel.removeFromHiddenApps(app = hiddenApp)

        val appsWithoutHidden = apps.map { it.toSelectedHiddenAppWith(isSelected = it != hiddenApp) }
        assertThat(viewModel.hideAppsState.awaitItem().hiddenApps).isEqualTo(appsWithoutHidden)
    }

    @Test
    fun `04 - when hidden apps are cleared, every item in the list must not be selected`() = runCoroutineTest {
        val apps = TestApps.all

        apps.forEach { viewModel.addToHiddenApps(it) }
        assertThat(viewModel.hideAppsState.awaitItem().hiddenApps).isEqualTo(apps.toSelectedHiddenAppWith(isSelected = true))

        viewModel.clearHiddenApps()
        assertThat(viewModel.hideAppsState.awaitItem().hiddenApps).isEqualTo(apps.toSelectedHiddenAppWith(isSelected = false))
    }

    @Test
    fun `05 - when a favorite app is being hidden, it must be removed from favorites list`() = runCoroutineTest {
        val allApps = TestApps.all
        val favoriteApp = TestApps.Phone

        favoritesRepo.addToFavorites(app = favoriteApp)

        var expected = allApps.map { it.toSelectedHiddenAppWith(isSelected = false, isFavorite = it == favoriteApp) }
        assertThat(viewModel.hideAppsState.awaitItem().hiddenApps).isEqualTo(expected)

        hiddenAppsRepo.addToHiddenApps(app = favoriteApp)

        expected = allApps.map { it.toSelectedHiddenAppWith(isSelected = it == favoriteApp, isFavorite = it == favoriteApp) }
        assertThat(viewModel.hideAppsState.awaitItem().hiddenApps).isEqualTo(expected)

        favoritesRepo.removeFromFavorites(packageName = favoriteApp.packageName)

        expected = allApps.map { it.toSelectedHiddenAppWith(isSelected = it == favoriteApp, isFavorite = false) }
        assertThat(viewModel.hideAppsState.awaitItem().hiddenApps).isEqualTo(expected)
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
