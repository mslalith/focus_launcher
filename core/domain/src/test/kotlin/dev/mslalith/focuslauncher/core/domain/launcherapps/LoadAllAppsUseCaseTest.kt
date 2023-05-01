package dev.mslalith.focuslauncher.core.domain.launcherapps

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.test.TestLauncherAppsManager
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class LoadAllAppsUseCaseTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    private val launcherAppsManager = spyk<TestLauncherAppsManager>()

    private lateinit var useCase: LoadAllAppsUseCase

    @Before
    fun setup() {
        hiltRule.inject()
        useCase = LoadAllAppsUseCase(
            launcherAppsManager = launcherAppsManager,
            appDrawerRepo = appDrawerRepo
        )
    }

    @Test
    fun `1 - when installed apps are queried and added, they must be added to apps DB`() = runCoroutineTest {
        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEmpty()

        launcherAppsManager.setAllApps(apps = TestApps.all)
        useCase()

        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEqualTo(TestApps.all)
    }

    @Test
    fun `2 - when force loading apps, new set of apps must be added to apps DB`() = runCoroutineTest {
        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEmpty()
        var appsAdded = listOf(TestApps.Chrome)

        launcherAppsManager.setAllApps(apps = appsAdded)
        useCase()
        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEqualTo(appsAdded)

        appsAdded = TestApps.all

        launcherAppsManager.setAllApps(apps = appsAdded)
        useCase(forceLoad = true)
        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEqualTo(appsAdded)
    }

    @Test
    fun `3 - when installed apps are loaded, they must not be loaded again`() = runCoroutineTest {
        launcherAppsManager.setAllApps(apps = TestApps.all)
        useCase()

        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEqualTo(TestApps.all)

        useCase()
        verify(exactly = 1) { launcherAppsManager.loadAllApps() }
    }

    @Test
    fun `4 - when installed apps are queried and added, they must be added to apps DB`() = runCoroutineTest {
        launcherAppsManager.setAllApps(apps = TestApps.all)
        useCase()

        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEqualTo(TestApps.all)

        useCase(forceLoad = true)
        verify(exactly = 2) { launcherAppsManager.loadAllApps() }
    }
}
