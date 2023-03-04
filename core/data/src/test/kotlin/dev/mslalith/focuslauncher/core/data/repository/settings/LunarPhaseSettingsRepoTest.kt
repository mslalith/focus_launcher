package dev.mslalith.focuslauncher.core.data.repository.settings

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.data.helpers.verifyChange
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_CURRENT_PLACE
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_ILLUMINATION_PERCENT
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_LUNAR_PHASE
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_UPCOMING_PHASE_DETAILS
import dev.mslalith.focuslauncher.core.model.City
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
internal class LunarPhaseSettingsRepoTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: LunarPhaseSettingsRepo

    @Before
    fun setup() {
        hiltRule.inject()
    }

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
