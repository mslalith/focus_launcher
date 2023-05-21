package dev.mslalith.focuslauncher.feature.favorites

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
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.extensions.assertFor
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import dev.mslalith.focuslauncher.core.testing.toPackageNamed
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class FavoritesViewModelTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getDefaultFavoriteAppsUseCase: GetDefaultFavoriteAppsUseCase

    @Inject
    lateinit var getFavoriteColoredAppsUseCase: GetFavoriteColoredAppsUseCase

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    @Inject
    lateinit var generalSettingsRepo: GeneralSettingsRepo

    @Inject
    lateinit var favoritesRepo: FavoritesRepo

    @Inject
    lateinit var appCoroutineDispatcher: AppCoroutineDispatcher

    private lateinit var viewModel: FavoritesViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = FavoritesViewModel(
            getDefaultFavoriteAppsUseCase = getDefaultFavoriteAppsUseCase,
            getFavoriteColoredAppsUseCase = getFavoriteColoredAppsUseCase,
            generalSettingsRepo = generalSettingsRepo,
            favoritesRepo = favoritesRepo,
            appCoroutineDispatcher = appCoroutineDispatcher
        )
    }

    @Test
    fun `01 - when apps are loaded and favorites are added, we should get the default favorites back`() = runCoroutineTest {
        val allApps = TestApps.all.toPackageNamed()
        val defaultApps = listOf(TestApps.Youtube).toPackageNamed()
        assertThat(viewModel.favoritesState.awaitItem().favoritesList).isEmpty()
        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEmpty()

        appDrawerRepo.addApps(apps = allApps)
        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEqualTo(allApps)

        favoritesRepo.addToFavorites(apps = defaultApps)
        viewModel.favoritesState.assertFor(expected = defaultApps) { it.favoritesList.map { it.app } }
    }

    @Test
    fun `02 - when apps are not loaded and favorites are added, we should get the default favorites back`() = runCoroutineTest {
        val allApps = TestApps.all.toPackageNamed()
        val defaultApps = listOf(TestApps.Youtube).toPackageNamed()
        favoritesRepo.addToFavorites(apps = defaultApps)

        assertThat(viewModel.favoritesState.awaitItem().favoritesList).isEmpty()
        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEmpty()

        appDrawerRepo.addApps(apps = allApps)
        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEqualTo(allApps)

        viewModel.favoritesState.assertFor(expected = defaultApps) { it.favoritesList.map { it.app } }
    }
}
