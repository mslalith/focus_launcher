package dev.mslalith.focuslauncher.core.data.repository.impl.settings

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.data.helpers.verifyChange
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.General.DEFAULT_FIRST_RUN
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.General.DEFAULT_IS_DEFAULT_LAUNCHER
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.General.DEFAULT_STATUS_BAR
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
internal class GeneralSettingsRepoTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: GeneralSettingsRepo

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun `verify first run change`() = runCoroutineTest {
        verifyChange(
            flow = repo.firstRunFlow,
            initialItem = DEFAULT_FIRST_RUN,
            mutate = {
                repo.overrideFirstRun()
                false
            }
        )
    }

    @Test
    fun `verify status bar visibility change`() = runCoroutineTest {
        verifyChange(
            flow = repo.statusBarVisibilityFlow,
            initialItem = DEFAULT_STATUS_BAR,
            mutate = {
                repo.toggleStatusBarVisibility()
                true
            }
        )
    }

    @Test
    fun `verify notification shade change`() = runCoroutineTest {
        verifyChange(
            flow = repo.notificationShadeFlow,
            initialItem = DEFAULT_NOTIFICATION_SHADE,
            mutate = {
                repo.toggleNotificationShade()
                false
            }
        )
    }

    @Test
    fun `verify is default launcher change`() = runCoroutineTest {
        verifyChange(
            flow = repo.isDefaultLauncher,
            initialItem = DEFAULT_IS_DEFAULT_LAUNCHER,
            mutate = {
                repo.setIsDefaultLauncher(true)
                true
            }
        )
    }
}
