package dev.mslalith.focuslauncher.core.domain.apps

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.domain.apps.core.GetAppsIconPackAwareUseCase
import dev.mslalith.focuslauncher.core.domain.apps.core.GetAppsUseCase
import dev.mslalith.focuslauncher.core.domain.iconpack.GetIconPackAppsUseCase
import dev.mslalith.focuslauncher.core.domain.utils.toIconPack
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.test.TestLauncherAppsManager
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.test.TestIconProvider
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.disableAsSystem
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import dev.mslalith.focuslauncher.core.testing.launcherapps.TestIconPackManager
import dev.mslalith.focuslauncher.core.testing.toPackageNamed
import kotlinx.coroutines.CoroutineScope
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
internal class GetIconPackIconicAppsUseCaseTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    @Inject
    lateinit var generalSettingsRepo: GeneralSettingsRepo

    private lateinit var getAppsUseCase: GetAppsUseCase
    private lateinit var getAppsIconPackAwareUseCase: GetAppsIconPackAwareUseCase
    private lateinit var getIconPackAppsUseCase: GetIconPackAppsUseCase
    private lateinit var useCase: GetIconPackIconicAppsUseCase

    private val testLauncherAppsManager = TestLauncherAppsManager()
    private val testIconPackManager = TestIconPackManager()
    private val testIconProvider = TestIconProvider

    @Before
    fun setup() {
        hiltRule.inject()
        getAppsUseCase = GetAppsUseCase(
            launcherAppsManager = testLauncherAppsManager,
            iconPackManager = testIconPackManager,
            iconProvider = testIconProvider
        )
        getAppsIconPackAwareUseCase = GetAppsIconPackAwareUseCase(
            getAppsUseCase = getAppsUseCase,
            generalSettingsRepo = generalSettingsRepo
        )
        getIconPackAppsUseCase = GetIconPackAppsUseCase(
            iconPackManager = testIconPackManager,
            appDrawerRepo = appDrawerRepo
        )
        useCase = GetIconPackIconicAppsUseCase(
            getAppsIconPackAwareUseCase = getAppsIconPackAwareUseCase,
            getIconPackAppsUseCase = getIconPackAppsUseCase
        )
    }

    @Test
    fun `01 - get all icon pack apps`() = runCoroutineTest {
        val allApps = TestApps.all.toPackageNamed().disableAsSystem()
        val expectedApps = allApps.take(n = 1)
        val iconPackApps = expectedApps.map(App::toIconPack)

        generalSettingsRepo.updateIconPackType(iconPackType = IconPackType.System)
        appDrawerRepo.addApps(apps = allApps)
        testIconPackManager.setIconPackApps(iconPacks = iconPackApps)

        assertIconPackApps(expected = expectedApps)
    }

    context (CoroutineScope)
    private suspend fun assertIconPackApps(expected: List<App>) {
        val expectedApps = expected.toPackageNamed().disableAsSystem().toAppWithIconFavorites()
        val actualApps = useCase().awaitItem()

        val expectedAppsWithoutIcon = expectedApps
            .sortedBy { it.app.packageName }
            .map { it.app }

        val actualAppsWithoutIcon = actualApps
            .sortedBy { it.app.packageName }
            .map { it.app }

        assertThat(actualAppsWithoutIcon).isEqualTo(expectedAppsWithoutIcon)
    }

    private suspend fun List<App>.toAppWithIconFavorites(): List<AppWithIcon> = map {
        AppWithIcon(
            icon = testIconProvider.iconFor(
                appWithComponent = testLauncherAppsManager.loadApp(packageName = it.packageName),
                iconPackType = IconPackType.System
            ),
            app = it
        )
    }
}
