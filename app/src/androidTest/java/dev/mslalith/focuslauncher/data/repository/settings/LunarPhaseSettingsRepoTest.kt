package dev.mslalith.focuslauncher.data.repository.settings

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.data.repository.DataStoreTest
import dev.mslalith.focuslauncher.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LunarPhaseSettingsRepoTest : DataStoreTest<LunarPhaseSettingsRepo>(
    setupRepo = { LunarPhaseSettingsRepo(it) }
) {

    @Test
    fun getShowLunarPhaseFlow() = runTest {
        val value = repo.showLunarPhaseFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_LUNAR_PHASE)
    }

    @Test
    fun getShowIlluminationPercentFlow() = runTest {
        val value = repo.showIlluminationPercentFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_ILLUMINATION_PERCENT)
    }

    @Test
    fun getShowUpcomingPhaseDetailsFlow() = runTest {
        val value = repo.showUpcomingPhaseDetailsFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_UPCOMING_PHASE_DETAILS)
    }

    @Test
    fun toggleShowLunarPhase() = runTest {
        repo.toggleShowLunarPhase()
        val value = repo.showLunarPhaseFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_LUNAR_PHASE)
    }

    @Test
    fun toggleShowIlluminationPercent() = runTest {
        repo.toggleShowIlluminationPercent()
        val value = repo.showIlluminationPercentFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_ILLUMINATION_PERCENT)
    }

    @Test
    fun toggleShowUpcomingPhaseDetails() = runTest {
        repo.toggleShowUpcomingPhaseDetails()
        val value = repo.showUpcomingPhaseDetailsFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_UPCOMING_PHASE_DETAILS)
    }
}
