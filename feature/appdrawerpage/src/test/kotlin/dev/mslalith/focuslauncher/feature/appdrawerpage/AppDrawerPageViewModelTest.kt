package dev.mslalith.focuslauncher.feature.appdrawerpage

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.LoadingState
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.getOrNull
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.AppDrawerSettingsRepo
import dev.mslalith.focuslauncher.core.domain.apps.GetAppDrawerIconicAppsUseCase
import dev.mslalith.focuslauncher.core.domain.iconpack.ReloadIconPackUseCase
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithIconFavorite
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.extensions.assertFor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
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
class AppDrawerPageViewModelTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getAppDrawerIconicAppsUseCase: GetAppDrawerIconicAppsUseCase

    @Inject
    lateinit var appDrawerSettingsRepo: AppDrawerSettingsRepo

    @Inject
    lateinit var reloadIconPackUseCase: ReloadIconPackUseCase

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    @Inject
    lateinit var favoritesRepo: FavoritesRepo

    @Inject
    lateinit var hiddenAppsRepo: HiddenAppsRepo

    @Inject
    lateinit var appCoroutineDispatcher: AppCoroutineDispatcher

    private lateinit var viewModel: AppDrawerPageViewModel

    private val allApps by lazy {
        TestApps.all.map { it.copy(name = it.packageName, displayName = it.packageName, isSystem = false) }
    }

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = AppDrawerPageViewModel(
            getAppDrawerIconicAppsUseCase = getAppDrawerIconicAppsUseCase,
            appDrawerSettingsRepo = appDrawerSettingsRepo,
            reloadIconPackUseCase = reloadIconPackUseCase,
            appDrawerRepo = appDrawerRepo,
            hiddenAppsRepo = hiddenAppsRepo,
            favoritesRepo = favoritesRepo,
            appCoroutineDispatcher = appCoroutineDispatcher
        )
        runBlocking {
            appDrawerRepo.addApps(apps = allApps)
        }
    }

    @Test
    fun `01 - when search input is given, apps should be filtered`() = runCoroutineTest {
        val chromeApp = allApps.first { it.packageName.contains("chrome") }

        viewModel.assertDrawerApps(expected = allApps)
        viewModel.searchAppQuery(query = chromeApp.name)
        viewModel.assertDrawerApps(expected = listOf(chromeApp))
    }

    @Test
    fun `02 - when search input is cleared, all apps must be shown`() = runCoroutineTest {
        val chromeApp = allApps.first { it.packageName.contains("chrome") }

        viewModel.assertDrawerApps(expected = allApps)
        viewModel.searchAppQuery(query = chromeApp.name)
        viewModel.assertDrawerApps(expected = listOf(chromeApp))

        viewModel.searchAppQuery(query = "")
        viewModel.assertDrawerApps(expected = allApps)
    }

    @Test
    fun `03 - when search input is given & search is disabled, all apps must be shown`() = runCoroutineTest {
        val chromeApp = allApps.first { it.packageName.contains("chrome") }

        viewModel.assertDrawerApps(expected = allApps)
        viewModel.searchAppQuery(query = chromeApp.name)
        viewModel.assertDrawerApps(expected = listOf(chromeApp))

        appDrawerSettingsRepo.toggleSearchBarVisibility()
        viewModel.assertDrawerApps(expected = allApps)
    }
}

context (CoroutineScope)
private suspend fun AppDrawerPageViewModel.assertDrawerApps(
    expected: List<App>
) {
    appDrawerPageState.assertFor(expected = expected) { it.allAppsState.toTestApps() }
}

private fun LoadingState<ImmutableList<AppWithIconFavorite>>.toTestApps(): List<App>? = getOrNull()?.map { it.appWithIcon.app }
