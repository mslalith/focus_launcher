package dev.mslalith.focuslauncher.screens.hideapps

import app.cash.turbine.test
import com.google.common.truth.Truth.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.screens.hideapps.model.HiddenApp
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
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
            appDrawerRepo.addApps(TestApps.all)
        }
    }

    @Test
    fun `1 - initially hidden apps must not be selected`() = runCoroutineTest {
        backgroundScope.launch {
            viewModel.hiddenAppsFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(TestApps.all.toHiddenAppWith(isSelected = false))
            }
        }
    }

    @Test
    fun `2 - when all apps are hidden, every item in the list must be selected`() = runCoroutineTest {
        val apps = TestApps.all

        backgroundScope.launch {
            viewModel.hiddenAppsFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(apps.toHiddenAppWith(isSelected = true))
            }
        }

        apps.forEach { viewModel.addToHiddenApps(it) }
    }

    @Test
    fun `3 - when hidden apps are cleared, every item in the list must not be selected`() = runCoroutineTest {
        val apps = TestApps.all

        backgroundScope.launch {
            viewModel.hiddenAppsFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(apps.toHiddenAppWith(isSelected = true))
                assertThat(awaitItem()).isEqualTo(apps.toHiddenAppWith(isSelected = false))
            }
        }

        apps.forEach { viewModel.addToHiddenApps(it) }
        viewModel.clearHiddenApps()
    }

    @Test
    fun `4 - when a favorite app is being hidden, it must be removed from favorites list`() = runCoroutineTest {
        val favoriteApp = TestApps.Phone
        val apps = TestApps.all - setOf(favoriteApp)
        favoritesRepo.addToFavorites(favoriteApp)

        backgroundScope.launch {
            viewModel.hiddenAppsFlow.test {
                assertThat(awaitItem()).isEmpty()

                assertThat(awaitItem()).isEqualTo(apps.toHiddenAppWith(isSelected = false, isFavorite = true))
                assertThat(awaitItem()).isEqualTo(apps.toHiddenAppWith(isSelected = true, isFavorite = false))
            }
        }

        hiddenAppsRepo.addToHiddenApps(favoriteApp)
        favoritesRepo.removeFromFavorites(favoriteApp.packageName)
    }
}

private fun App.toHiddenAppWith(
    isSelected: Boolean = false,
    isFavorite: Boolean = false
): HiddenApp = HiddenApp(app = this, isSelected = isSelected, isFavorite = isFavorite)

private fun List<App>.toHiddenAppWith(
    isSelected: Boolean = false,
    isFavorite: Boolean = false
): List<HiddenApp> = map { it.toHiddenAppWith(isSelected = isSelected, isFavorite = isFavorite) }
