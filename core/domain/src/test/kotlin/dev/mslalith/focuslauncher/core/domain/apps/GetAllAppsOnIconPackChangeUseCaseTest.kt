package dev.mslalith.focuslauncher.core.domain.apps

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.domain.apps.core.GetAppsUseCase
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.test.TestLauncherAppsManager
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.test.TestIconProvider
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.disableAsSystem
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import dev.mslalith.focuslauncher.core.testing.toPackageNamed
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
internal class GetAllAppsOnIconPackChangeUseCaseTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getAppsUseCase: GetAppsUseCase

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    @Inject
    lateinit var favoritesRepo: FavoritesRepo

    private lateinit var useCase: GetAllAppsOnIconPackChangeUseCase

    private val testLauncherAppsManager by lazy { TestLauncherAppsManager() }
    private val iconProvider by lazy { TestIconProvider }

    private val allApps by lazy { TestApps.all.toPackageNamed().disableAsSystem() }

    @Before
    fun setup() {
        hiltRule.inject()
        useCase = GetAllAppsOnIconPackChangeUseCase(
            getAppsUseCase = getAppsUseCase,
            appDrawerRepo = appDrawerRepo,
            favoritesRepo = favoritesRepo
        )
        runBlocking { appDrawerRepo.addApps(apps = allApps) }
    }

    @Test
    fun `01 - get all icon pack apps`() = runCoroutineTest {
        assertIconPackApps(expected = allApps)
    }

    context (CoroutineScope)
    private suspend fun assertIconPackApps(expected: List<App>) {
        val expectedApps = expected.toPackageNamed().disableAsSystem().toAppDrawerItems()
        val actualApps = useCase(iconPackType = IconPackType.System).awaitItem()

        val expectedAppsWithoutIcon = expectedApps
            .sortedBy { it.app.packageName }
            .map { it.app to it.isFavorite }

        val actualAppsWithoutIcon = actualApps
            .sortedBy { it.app.packageName }
            .map { it.app to it.isFavorite }

        assertThat(actualAppsWithoutIcon).isEqualTo(expectedAppsWithoutIcon)
    }

    private fun List<App>.toAppDrawerItems(): List<AppDrawerItem> = map {
        AppDrawerItem(
            app = it,
            isFavorite = false,
            icon = iconProvider.iconFor(
                appWithComponent = testLauncherAppsManager.loadApp(packageName = it.packageName),
                iconPackType = IconPackType.System
            ),
            color = null
        )
    }
}
