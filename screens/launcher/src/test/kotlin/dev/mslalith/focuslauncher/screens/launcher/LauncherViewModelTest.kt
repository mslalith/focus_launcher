package dev.mslalith.focuslauncher.screens.launcher

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.TestLauncherAppsManager
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
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
class LauncherViewModelTest : CoroutineTest() {

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

    private val launcherAppsManager = TestLauncherAppsManager()

    private lateinit var viewModel: LauncherViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = LauncherViewModel(
            launcherAppsManager = launcherAppsManager,
            appDrawerRepo = appDrawerRepo,
            favoritesRepo = favoritesRepo,
            hiddenAppsRepo = hiddenAppsRepo,
            appCoroutineDispatcher = appCoroutineDispatcher
        )
    }

    @Test
    fun `1 - initially apps must be empty`() = runCoroutineTest {
        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEmpty()
    }

    @Test
    fun `2 - when installed apps are queried and added, they must be added to apps DB`() = runCoroutineTest {
        backgroundScope.launch {
            appDrawerRepo.allAppsFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(TestApps.all)
            }
        }

        viewModel.loadApps()
    }

    @Test
    fun `3 - when an app is installed, it must be added to apps DB`() = runCoroutineTest {
        val appToInstall = TestApps.Chrome
        val allApps = TestApps.all
        val installedApps = allApps - setOf(appToInstall)
        appDrawerRepo.addApps(installedApps)

        backgroundScope.launch {
            appDrawerRepo.allAppsFlow.test {
                assertThat(awaitItem()).isEqualTo(installedApps)
                assertThat(awaitItem()).isEqualTo(allApps)
            }
        }

        viewModel.handleAppInstall(appToInstall)
    }

    @Test
    fun `4 - when an app is uninstalled, it must be removed from apps DB`() = runCoroutineTest {
        val appToUninstall = TestApps.Chrome
        val allApps = TestApps.all
        val appsAfterUninstall = allApps - setOf(appToUninstall)
        appDrawerRepo.addApps(allApps)

        backgroundScope.launch {
            appDrawerRepo.allAppsFlow.test {
                assertThat(awaitItem()).isEqualTo(allApps)
                assertThat(awaitItem()).isEqualTo(appsAfterUninstall)
            }
        }

        viewModel.handleAppUninstall(appToUninstall.packageName)
    }
}
