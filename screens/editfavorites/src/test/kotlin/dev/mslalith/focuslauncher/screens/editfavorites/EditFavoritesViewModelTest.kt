package dev.mslalith.focuslauncher.screens.editfavorites

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.model.SelectedApp
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
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
class EditFavoritesViewModelTest : CoroutineTest() {

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

    private lateinit var viewModel: EditFavoritesViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = EditFavoritesViewModel(
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
    fun `1 - initially favorites must not be selected`() = runCoroutineTest {
        backgroundScope.launch {
            viewModel.favoritesStateFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(TestApps.all.toSelectedAppWith(isSelected = false))
            }
        }
    }

    @Test
    fun `2 - when all apps are added to favorite, every item in the list must be selected`() = runCoroutineTest {
        val apps = TestApps.all

        backgroundScope.launch {
            viewModel.favoritesStateFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(apps.toSelectedAppWith(isSelected = true))
            }
        }

        apps.forEach { viewModel.addToFavorites(it) }
    }

    @Test
    fun `3 - when favorites are cleared, every item in the list must not be selected`() = runCoroutineTest {
        val apps = TestApps.all

        backgroundScope.launch {
            viewModel.favoritesStateFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(apps.toSelectedAppWith(isSelected = true))
                assertThat(awaitItem()).isEqualTo(apps.toSelectedAppWith(isSelected = false))
            }
        }

        apps.forEach { viewModel.addToFavorites(it) }
        viewModel.clearFavorites()
    }

    @Test
    fun `4 - when hidden apps are not shown & an app is hidden, it should not be listed in favorites`() = runCoroutineTest {
        val hiddenApps = listOf(TestApps.Phone, TestApps.Chrome)
        val apps = TestApps.all - hiddenApps.toSet()

        backgroundScope.launch {
            viewModel.favoritesStateFlow.test {
                assertThat(awaitItem()).isEmpty()

                val actualApps = awaitItem()
                assertThat(actualApps.size).isEqualTo(apps.size)
                assertThat(actualApps).isEqualTo(apps.toSelectedAppWith(isSelected = false))
            }
        }

        hiddenAppsRepo.addToHiddenApps(hiddenApps)
    }

    @Test
    fun `5 - when hidden apps are shown & an app is hidden, it should be listed in favorites as disabled`() = runCoroutineTest {
        val hiddenApps = listOf(TestApps.Phone, TestApps.Chrome)
        val totalApps = TestApps.all
        val apps = totalApps - hiddenApps.toSet()

        backgroundScope.launch {
            viewModel.favoritesStateFlow.test {
                assertThat(awaitItem()).isEmpty()

                var actualApps = awaitItem()
                assertThat(actualApps.size).isEqualTo(apps.size)
                assertThat(actualApps).isEqualTo(apps.toSelectedAppWith(isSelected = false))

                actualApps = awaitItem()
                assertThat(actualApps.size).isEqualTo(totalApps.size)
                assertThat(actualApps).isEqualTo(totalApps.toSelectedAppWith(isSelected = false))
            }
        }

        hiddenAppsRepo.addToHiddenApps(hiddenApps)
        viewModel.shouldShowHiddenAppsInFavorites(value = true)
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
