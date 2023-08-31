package dev.mslalith.focuslauncher.core.domain.apps

import android.content.ComponentName
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.domain.PackageActionUseCase
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.test.TestLauncherAppsManager
import dev.mslalith.focuslauncher.core.model.PackageAction
import dev.mslalith.focuslauncher.core.model.app.AppWithComponent
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.disableAsSystem
import dev.mslalith.focuslauncher.core.testing.extensions.assertFor
import dev.mslalith.focuslauncher.core.testing.toPackageNamed
import io.mockk.coEvery
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AppRobolectricTestRunner::class)
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

    private val testLauncherAppsManager = spyk<TestLauncherAppsManager>()

    private lateinit var packageActionUseCase: PackageActionUseCase

    private val allApps by lazy { TestApps.all.toPackageNamed().disableAsSystem() }

    @Before
    fun setup() {
        hiltRule.inject()
        packageActionUseCase = PackageActionUseCase(
            launcherAppsManager = testLauncherAppsManager,
            appDrawerRepo = appDrawerRepo,
            favoritesRepo = favoritesRepo,
            hiddenAppsRepo = hiddenAppsRepo,
            appCoroutineDispatcher = appCoroutineDispatcher
        )
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `01 - when an app is installed, it must be added to apps DB`() = runCoroutineTest {
        val appToInstall = TestApps.Chrome.toPackageNamed().disableAsSystem()
        val installedApps = allApps - setOf(appToInstall)

        appDrawerRepo.addApps(apps = installedApps)
        appDrawerRepo.allAppsFlow.assertFor(expected = installedApps)

        packageActionUseCase(packageAction = PackageAction.Added(packageName = appToInstall.packageName))
        appDrawerRepo.allAppsFlow.assertFor(expected = allApps)
    }

    @Test
    fun `02 - when an app is uninstalled, it must be removed from apps DB`() = runCoroutineTest {
        val appToUninstall = TestApps.Chrome.toPackageNamed().disableAsSystem()
        val appsAfterUninstall = allApps - setOf(appToUninstall)

        appDrawerRepo.addApps(apps = allApps)
        appDrawerRepo.allAppsFlow.assertFor(expected = allApps)

        packageActionUseCase(packageAction = PackageAction.Removed(packageName = appToUninstall.packageName))
        appDrawerRepo.allAppsFlow.assertFor(expected = appsAfterUninstall)
    }

    @Test
    fun `03 - when a favorite app is uninstalled, it must be removed from DB`() = runCoroutineTest {
        val appToUninstall = TestApps.Chrome.toPackageNamed().disableAsSystem()
        appDrawerRepo.addApps(apps = allApps)
        favoritesRepo.addToFavorites(app = appToUninstall)

        favoritesRepo.onlyFavoritesFlow.assertFor(expected = listOf(appToUninstall))
        packageActionUseCase(packageAction = PackageAction.Removed(packageName = appToUninstall.packageName))
        favoritesRepo.onlyFavoritesFlow.assertFor(expected = listOf())
    }

    @Test
    fun `04 - when a hidden app is uninstalled, it must be removed from DB`() = runCoroutineTest {
        val appToUninstall = TestApps.Chrome.toPackageNamed().disableAsSystem()
        appDrawerRepo.addApps(apps = allApps)

        hiddenAppsRepo.addToHiddenApps(app = appToUninstall)
        hiddenAppsRepo.onlyHiddenAppsFlow.assertFor(expected = listOf(appToUninstall))

        packageActionUseCase(packageAction = PackageAction.Removed(packageName = appToUninstall.packageName))
        hiddenAppsRepo.onlyHiddenAppsFlow.assertFor(expected = listOf())
    }

    @Test
    fun `05 - when an app is updated, it must be updated in apps DB`() = runCoroutineTest {
        val appBeforeUpdate = TestApps.Chrome.toPackageNamed().disableAsSystem()
        val appAfterUpdate = appBeforeUpdate.copy(name = "Updated Chrome", displayName = "Updated Chrome")
        val installedApps = allApps

        appDrawerRepo.addApps(apps = installedApps)
        appDrawerRepo.allAppsFlow.assertFor(expected = installedApps)

        // provide updated app when updating
        coEvery { testLauncherAppsManager.loadApp(packageName = appAfterUpdate.packageName) } returns AppWithComponent(
            app = appAfterUpdate,
            componentName = ComponentName(appAfterUpdate.packageName, "")
        )
        packageActionUseCase(packageAction = PackageAction.Updated(packageName = appAfterUpdate.packageName))

        // build expected apps list
        val updatedExpectedApps = allApps.map { app ->
            if (app.packageName == appBeforeUpdate.packageName) appAfterUpdate else app
        }.sortedBy { it.packageName }

        appDrawerRepo.allAppsFlow.assertFor(expected = updatedExpectedApps) { apps ->
            apps.sortedBy { it.packageName }
        }
    }
}
