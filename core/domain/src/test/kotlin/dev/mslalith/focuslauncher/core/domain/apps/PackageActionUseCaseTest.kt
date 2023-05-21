package dev.mslalith.focuslauncher.core.domain.apps

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.domain.PackageActionUseCase
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import io.mockk.mockk
import javax.inject.Inject
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
class PackageActionUseCaseTest : CoroutineTest() {

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

    private lateinit var packageActionUseCase: PackageActionUseCase

    @Before
    fun setup() {
        hiltRule.inject()
        packageActionUseCase = PackageActionUseCase(
            launcherAppsManager = mockk(),
            appDrawerRepo = appDrawerRepo,
            favoritesRepo = favoritesRepo,
            hiddenAppsRepo = hiddenAppsRepo,
            appCoroutineDispatcher = appCoroutineDispatcher
        )
    }

    @Test
    fun `01 - when an app is installed, it must be added to apps DB`() = runCoroutineTest {
        val appToInstall = TestApps.Chrome
        val allApps = TestApps.all
        val installedApps = allApps - setOf(appToInstall)
        appDrawerRepo.addApps(apps = installedApps)

        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEqualTo(installedApps)
        packageActionUseCase.handleAppInstall(app = appToInstall)
        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEqualTo(allApps)
    }

    @Test
    fun `02 - when an app is uninstalled, it must be removed from apps DB`() = runCoroutineTest {
        val appToUninstall = TestApps.Chrome
        val allApps = TestApps.all
        val appsAfterUninstall = allApps - setOf(appToUninstall)
        appDrawerRepo.addApps(apps = allApps)

        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEqualTo(allApps)
        packageActionUseCase.handleAppUninstall(packageName = appToUninstall.packageName)
        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEqualTo(appsAfterUninstall)
    }

    @Test
    fun `03 - when a favorite app is uninstalled, it must be removed from DB`() = runCoroutineTest {
        val appToUninstall = TestApps.Chrome
        val allApps = TestApps.all
        appDrawerRepo.addApps(apps = allApps)
        favoritesRepo.addToFavorites(app = appToUninstall)

        assertThat(favoritesRepo.onlyFavoritesFlow.awaitItem()).isEqualTo(listOf(appToUninstall))
        packageActionUseCase.handleAppUninstall(packageName = appToUninstall.packageName)
        assertThat(favoritesRepo.onlyFavoritesFlow.awaitItem()).isEmpty()
    }

    @Test
    fun `04 - when a hidden app is uninstalled, it must be removed from DB`() = runCoroutineTest {
        val appToUninstall = TestApps.Chrome
        val allApps = TestApps.all
        appDrawerRepo.addApps(apps = allApps)
        hiddenAppsRepo.addToHiddenApps(app = appToUninstall)

        assertThat(hiddenAppsRepo.onlyHiddenAppsFlow.awaitItem()).isEqualTo(listOf(appToUninstall))
        packageActionUseCase.handleAppUninstall(packageName = appToUninstall.packageName)
        assertThat(hiddenAppsRepo.onlyHiddenAppsFlow.awaitItem()).isEmpty()
    }
}
