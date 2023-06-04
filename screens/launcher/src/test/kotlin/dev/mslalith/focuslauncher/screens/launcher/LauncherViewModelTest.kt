package dev.mslalith.focuslauncher.screens.launcher

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.database.usecase.room.CloseDatabaseUseCase
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.domain.launcherapps.LoadAllAppsUseCase
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.test.TestLauncherAppsManager
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.extensions.assertFor
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import dev.mslalith.focuslauncher.core.testing.toAppsWithComponents
import org.junit.After
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
internal class LauncherViewModelTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    @Inject
    lateinit var closeDatabaseUseCase: CloseDatabaseUseCase

    @Inject
    lateinit var appCoroutineDispatcher: AppCoroutineDispatcher

    private val testLauncherAppsManager = TestLauncherAppsManager()

    private lateinit var viewModel: LauncherViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = LauncherViewModel(
            loadAllAppsUseCase = LoadAllAppsUseCase(
                launcherAppsManager = testLauncherAppsManager,
                appDrawerRepo = appDrawerRepo
            ),
            appCoroutineDispatcher = appCoroutineDispatcher
        )
    }

    @After
    fun teardown() {
        closeDatabaseUseCase()
    }

    @Test
    fun `01 - when queried, all apps must be retrieved`() = runCoroutineTest {
        assertThat(appDrawerRepo.allAppsFlow.awaitItem()).isEmpty()

        testLauncherAppsManager.setAllApps(apps = TestApps.all.toAppsWithComponents())
        viewModel.loadApps()

        appDrawerRepo.allAppsFlow.assertFor(expected = TestApps.all)
    }
}
