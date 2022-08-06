package dev.mslalith.focuslauncher.data.repository.settings

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.androidtest.shared.DataStoreTest
import dev.mslalith.focuslauncher.data.serializers.CityJsonParser
import dev.mslalith.focuslauncher.data.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class LunarPhaseSettingsRepoTest : DataStoreTest<LunarPhaseSettingsRepo>(
    setupRepo = { dataStore ->
        LunarPhaseSettingsRepo(
            settingsDataStore = dataStore,
            cityJsonParser = CityJsonParser()
        )
    }
) {

    @Test
    fun getShowLunarPhaseFlow() = runCoroutineTest {
        val value = repo.showLunarPhaseFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_LUNAR_PHASE)
    }

    @Test
    fun getShowIlluminationPercentFlow() = runCoroutineTest {
        val value = repo.showIlluminationPercentFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_ILLUMINATION_PERCENT)
    }

    @Test
    fun getShowUpcomingPhaseDetailsFlow() = runCoroutineTest {
        val value = repo.showUpcomingPhaseDetailsFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_UPCOMING_PHASE_DETAILS)
    }

    @Test
    fun toggleShowLunarPhase() = runCoroutineTest {
        repo.toggleShowLunarPhase()
        val value = repo.showLunarPhaseFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_LUNAR_PHASE)
    }

    @Test
    fun toggleShowIlluminationPercent() = runCoroutineTest {
        repo.toggleShowIlluminationPercent()
        val value = repo.showIlluminationPercentFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_ILLUMINATION_PERCENT)
    }

    @Test
    fun toggleShowUpcomingPhaseDetails() = runCoroutineTest {
        repo.toggleShowUpcomingPhaseDetails()
        val value = repo.showUpcomingPhaseDetailsFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_UPCOMING_PHASE_DETAILS)
    }
}
