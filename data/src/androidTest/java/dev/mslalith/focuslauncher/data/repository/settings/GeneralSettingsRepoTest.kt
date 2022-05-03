package dev.mslalith.focuslauncher.data.repository.settings

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.androidtest.shared.DataStoreTest
import dev.mslalith.focuslauncher.data.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GeneralSettingsRepoTest : DataStoreTest<GeneralSettingsRepo>(
    setupRepo = { GeneralSettingsRepo(it) }
) {

    @Test
    fun getFirstRunFlow() = runTest {
        val value = repo.firstRunFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.General.DEFAULT_FIRST_RUN)
    }

    @Test
    fun getStatusBarVisibilityFlow() = runTest {
        val value = repo.statusBarVisibilityFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.General.DEFAULT_STATUS_BAR)
    }

    @Test
    fun getNotificationShadeFlow() = runTest {
        val value = repo.notificationShadeFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE)
    }

    @Test
    fun overrideFirstRun() = runTest {
        repo.overrideFirstRun()
        val value = repo.firstRunFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.General.DEFAULT_FIRST_RUN)
    }

    @Test
    fun toggleStatusBarVisibility() = runTest {
        repo.toggleStatusBarVisibility()
        val value = repo.statusBarVisibilityFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.General.DEFAULT_STATUS_BAR)
    }

    @Test
    fun toggleNotificationShade() = runTest {
        repo.toggleNotificationShade()
        val value = repo.notificationShadeFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE)
    }
}
