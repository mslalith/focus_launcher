package dev.mslalith.focuslauncher.data.repository.settings

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.androidtest.shared.DataStoreTest
import dev.mslalith.focuslauncher.data.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class GeneralSettingsRepoTest : DataStoreTest<GeneralSettingsRepo>(
    setupRepo = { GeneralSettingsRepo(it) }
) {

    @Test
    fun getFirstRunFlow() = runCoroutineTest {
        val value = repo.firstRunFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.General.DEFAULT_FIRST_RUN)
    }

    @Test
    fun getStatusBarVisibilityFlow() = runCoroutineTest {
        val value = repo.statusBarVisibilityFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.General.DEFAULT_STATUS_BAR)
    }

    @Test
    fun getNotificationShadeFlow() = runCoroutineTest {
        val value = repo.notificationShadeFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE)
    }

    @Test
    fun getIsDefaultLauncher() = runCoroutineTest {
        val value = repo.isDefaultLauncher.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.General.DEFAULT_IS_DEFAULT_LAUNCHER)
    }

    @Test
    fun overrideFirstRun() = runCoroutineTest {
        repo.overrideFirstRun()
        val value = repo.firstRunFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.General.DEFAULT_FIRST_RUN)
    }

    @Test
    fun toggleStatusBarVisibility() = runCoroutineTest {
        repo.toggleStatusBarVisibility()
        val value = repo.statusBarVisibilityFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.General.DEFAULT_STATUS_BAR)
    }

    @Test
    fun toggleNotificationShade() = runCoroutineTest {
        repo.toggleNotificationShade()
        val value = repo.notificationShadeFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE)
    }

    @Test
    fun setIsDefaultLauncher() = runCoroutineTest {
        repo.setIsDefaultLauncher(true)
        val value = repo.isDefaultLauncher.first()
        assertThat(value).isEqualTo(true)
    }
}
