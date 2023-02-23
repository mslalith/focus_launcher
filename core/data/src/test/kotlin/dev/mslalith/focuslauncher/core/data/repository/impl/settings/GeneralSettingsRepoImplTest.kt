package dev.mslalith.focuslauncher.core.data.repository.impl.settings

import dev.mslalith.focuslauncher.core.data.base.RepoTest
import dev.mslalith.focuslauncher.core.data.model.TestComponents
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.General.DEFAULT_FIRST_RUN
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.General.DEFAULT_IS_DEFAULT_LAUNCHER
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.General.DEFAULT_STATUS_BAR
import dev.mslalith.focuslauncher.core.data.verifyChange
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
internal class GeneralSettingsRepoImplTest : RepoTest<GeneralSettingsRepoImpl>() {

    override fun provideRepo(testComponents: TestComponents) = GeneralSettingsRepoImpl(settingsDataStore = testComponents.dataStore)

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
