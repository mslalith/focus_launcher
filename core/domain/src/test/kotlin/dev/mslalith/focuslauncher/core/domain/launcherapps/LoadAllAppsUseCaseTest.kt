package dev.mslalith.focuslauncher.core.domain.launcherapps

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.test.TestLauncherAppsManager
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class LoadAllAppsUseCaseTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    private lateinit var useCase: LoadAllAppsUseCase

    @Before
    fun setup() {
        hiltRule.inject()
        useCase = LoadAllAppsUseCase(
            launcherAppsManager = TestLauncherAppsManager(),
            appDrawerRepo = appDrawerRepo
        )
    }

    @Test
    fun `when installed apps are queried and added, they must be added to apps DB`() = runCoroutineTest {
        backgroundScope.launch {
            appDrawerRepo.allAppsFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(TestApps.all)
            }
        }

        useCase()
    }
}