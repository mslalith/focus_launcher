package dev.mslalith.focuslauncher.core.data.repository.impl.settings

import dev.mslalith.focuslauncher.core.data.serializers.CityJsonParser
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_CURRENT_PLACE
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_ILLUMINATION_PERCENT
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_LUNAR_PHASE
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_UPCOMING_PHASE_DETAILS
import dev.mslalith.focuslauncher.core.data.verifyChange
import dev.mslalith.focuslauncher.core.model.City
import dev.mslalith.focuslauncher.core.testing.DataStoreTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
internal class LunarPhaseSettingsRepoImplTest : DataStoreTest<LunarPhaseSettingsRepoImpl>(
    setupRepo = {
        LunarPhaseSettingsRepoImpl(
            settingsDataStore = it,
            cityJsonParser = CityJsonParser()
        )
    }
) {

    @Test
    fun `verify show lunar phase change`() = runCoroutineTest {
        verifyChange(
            flow = repo.showLunarPhaseFlow,
            initialItem = DEFAULT_SHOW_LUNAR_PHASE,
            mutate = {
                repo.toggleShowLunarPhase()
                false
            }
        )
    }

    @Test
    fun `verify show illumination percent change`() = runCoroutineTest {
        verifyChange(
            flow = repo.showIlluminationPercentFlow,
            initialItem = DEFAULT_SHOW_ILLUMINATION_PERCENT,
            mutate = {
                repo.toggleShowIlluminationPercent()
                false
            }
        )
    }

    @Test
    fun `verify show upcoming phase details change`() = runCoroutineTest {
        verifyChange(
            flow = repo.showUpcomingPhaseDetailsFlow,
            initialItem = DEFAULT_SHOW_UPCOMING_PHASE_DETAILS,
            mutate = {
                repo.toggleShowUpcomingPhaseDetails()
                false
            }
        )
    }

    @Test
    fun `verify place change`() = runCoroutineTest {
        verifyChange(
            flow = repo.currentPlaceFlow,
            initialItem = DEFAULT_CURRENT_PLACE,
            mutate = {
                val newCity = City(
                    id = 13,
                    name = "Test",
                    latitude = 34.87,
                    longitude = 56.23
                )
                repo.updatePlace(city = newCity)
                newCity
            }
        )
    }
}
