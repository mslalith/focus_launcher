package dev.mslalith.focuslauncher.core.domain.favorites

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import io.mockk.mockk
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class PackageActionUseCaseTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    @Inject
    lateinit var favoritesRepo: FavoritesRepo

    @Inject
    lateinit var hiddenAppsRepo: HiddenAppsRepo

    private lateinit var packageActionUseCase: PackageActionUseCase

    @Before
    fun setup() {
        hiltRule.inject()
        packageActionUseCase = PackageActionUseCase(
            launcherAppsManager = mockk(),
            appDrawerRepo = appDrawerRepo,
            favoritesRepo = favoritesRepo,
            hiddenAppsRepo = hiddenAppsRepo
        )
    }

    @Test
    fun `when an app is installed, it must be added to apps DB`() = runCoroutineTest {
        val appToInstall = TestApps.Chrome
        val allApps = TestApps.all
        val installedApps = allApps - setOf(appToInstall)
        appDrawerRepo.addApps(apps = installedApps)

        backgroundScope.launch {
            appDrawerRepo.allAppsFlow.test {
                assertThat(awaitItem()).isEqualTo(installedApps)
                assertThat(awaitItem()).isEqualTo(allApps)
            }
        }

        packageActionUseCase.handleAppInstall(app = appToInstall)
    }

    @Test
    fun `when an app is uninstalled, it must be removed from apps DB`() = runCoroutineTest {
        val appToUninstall = TestApps.Chrome
        val allApps = TestApps.all
        val appsAfterUninstall = allApps - setOf(appToUninstall)
        appDrawerRepo.addApps(apps = allApps)

        backgroundScope.launch {
            appDrawerRepo.allAppsFlow.test {
                assertThat(awaitItem()).isEqualTo(allApps)
                assertThat(awaitItem()).isEqualTo(appsAfterUninstall)
            }
        }

        packageActionUseCase.handleAppUninstall(packageName = appToUninstall.packageName)
    }
}
