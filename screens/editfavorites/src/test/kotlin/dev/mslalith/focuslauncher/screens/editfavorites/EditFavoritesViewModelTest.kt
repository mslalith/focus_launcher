package dev.mslalith.focuslauncher.screens.editfavorites

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.SelectedApp
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
            appDrawerRepo.addApps(apps = TestApps.all)
        }
    }

    @Test
    fun `01 - initially favorites must not be selected`() = runCoroutineTest {
        assertThat(viewModel.favoritesStateFlow.value).isEmpty()
        assertThat(viewModel.favoritesStateFlow.awaitItem()).isEqualTo(TestApps.all.toSelectedAppWith(isSelected = false))
    }

    @Test
    fun `02 - when all apps are added to favorite, every item in the list must be selected`() = runCoroutineTest {
        val apps = TestApps.all

        apps.forEach { viewModel.addToFavorites(app = it) }
        assertThat(viewModel.favoritesStateFlow.awaitItem()).isEqualTo(apps.toSelectedAppWith(isSelected = true))
    }

    @Test
    fun `03 - when favorites are cleared, every item in the list must not be selected`() = runCoroutineTest {
        val apps = TestApps.all

        apps.forEach { viewModel.addToFavorites(app = it) }
        assertThat(viewModel.favoritesStateFlow.awaitItem()).isEqualTo(apps.toSelectedAppWith(isSelected = true))

        viewModel.clearFavorites()
        assertThat(viewModel.favoritesStateFlow.awaitItem()).isEqualTo(apps.toSelectedAppWith(isSelected = false))
    }

    @Test
    fun `04 - when hidden apps are not shown & an app is hidden, it should not be listed in favorites`() = runCoroutineTest {
        val hiddenApps = listOf(TestApps.Phone, TestApps.Chrome)
        val apps = TestApps.all - hiddenApps.toSet()

        hiddenAppsRepo.addToHiddenApps(apps = hiddenApps)
        val actualApps = viewModel.favoritesStateFlow.awaitItem()
        assertThat(actualApps.size).isEqualTo(apps.size)
        assertThat(actualApps).isEqualTo(apps.toSelectedAppWith(isSelected = false))
    }

    @Test
    fun `05 - when hidden apps are shown & an app is hidden, it should be listed in favorites as disabled`() = runCoroutineTest {
        val hiddenApps = listOf(TestApps.Phone, TestApps.Chrome)
        val totalApps = TestApps.all
        val apps = totalApps - hiddenApps.toSet()

        hiddenAppsRepo.addToHiddenApps(apps = hiddenApps)

        var actualApps = viewModel.favoritesStateFlow.awaitItem()
        assertThat(actualApps.size).isEqualTo(apps.size)
        assertThat(actualApps).isEqualTo(apps.toSelectedAppWith(isSelected = false))

        viewModel.shouldShowHiddenAppsInFavorites(value = true)

        actualApps = viewModel.favoritesStateFlow.awaitItem()
        val expected = totalApps.map { it.toSelectedAppWith(isSelected = false, disabled = hiddenApps.contains(it)) }
        assertThat(actualApps.size).isEqualTo(expected.size)
        assertThat(actualApps).isEqualTo(expected)
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
