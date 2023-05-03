package dev.mslalith.focuslauncher.feature.favorites

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.domain.appswithicons.GetFavoriteAppsWithColorUseCase
import dev.mslalith.focuslauncher.core.domain.launcherapps.GetDefaultFavoriteAppsUseCase
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItemChange
import dev.mslalith.focuslauncher.feature.favorites.model.FavoritesState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
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
    lateinit var getFavoriteAppsWithColorUseCase: GetFavoriteAppsWithColorUseCase

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
            getFavoriteAppsWithColorUseCase = getFavoriteAppsWithColorUseCase,
            generalSettingsRepo = generalSettingsRepo,
            favoritesRepo = favoritesRepo,
            appCoroutineDispatcher = appCoroutineDispatcher
        )
    }

    @Test
    fun `when apps are loaded and favorites are added, we should get the default favorites back`() = runCoroutineTest {
        val defaultApps = listOf(TestApps.Youtube)
        assertThat(viewModel.favoritesState.awaitItem().favoritesList).isEmpty()

        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEmpty()
        appDrawerRepo.addApps(apps = TestApps.all)
        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEqualTo(TestApps.all)

        favoritesRepo.addToFavorites(apps = defaultApps)
        viewModel.favoritesState.assertFavoritesList(expected = defaultApps)
    }

    @Test
    fun `when apps are not loaded and favorites are added, we should get the default favorites back`() = runCoroutineTest {
        val defaultApps = listOf(TestApps.Youtube)
        favoritesRepo.addToFavorites(apps = defaultApps)
        assertThat(viewModel.favoritesState.awaitItem().favoritesList).isEmpty()

        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEmpty()
        appDrawerRepo.addApps(apps = TestApps.all)
        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEqualTo(TestApps.all)

        viewModel.favoritesState.assertFavoritesList(expected = defaultApps)
    }
}

context (CoroutineScope)
private suspend fun StateFlow<FavoritesState>.assertFavoritesList(expected: List<App>) {
    val actual = awaitItemChange { it.favoritesList.toImmutableList() }.map { it.app }
    assertThat(actual).isEqualTo(expected)
}
