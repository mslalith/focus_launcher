package dev.mslalith.focuslauncher.data.repository

import app.cash.turbine.testIn
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.androidtest.shared.awaitItemAndCancel
import dev.mslalith.focuslauncher.core.model.City
import dev.mslalith.focuslauncher.data.model.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class LunarPhaseDetailsRepoTest {

    private lateinit var lunarPhaseDetailsRepo: LunarPhaseDetailsRepo

    private val city = City(
        id = -1,
        name = "Test",
        latitude = 34.345345,
        longitude = 65.452342
    )

    @Before
    fun setUp() {
        lunarPhaseDetailsRepo = LunarPhaseDetailsRepo()
    }

    @After
    fun tearDown() {
    }

    private fun getConsecutiveInstants(max: Int): List<Instant> = buildList {
        val instant = Clock.System.now()
        add(instant)
        (1 until max).forEach { index ->
            val duration = index.seconds
            add(instant.plus(duration))
        }
    }

    @Test
    fun getLunarPhaseDetailsStateFlow() = runTest {
        val instants = getConsecutiveInstants(max = 2)

        var lunarPhaseDetails = lunarPhaseDetailsRepo.lunarPhaseDetailsStateFlow.testIn(this).awaitItemAndCancel()
        assertThat(lunarPhaseDetails).isInstanceOf(State.Error::class.java)

        instants.forEach { instant ->
            lunarPhaseDetailsRepo.refreshLunarPhaseDetails(instant, city)
            lunarPhaseDetails = lunarPhaseDetailsRepo.lunarPhaseDetailsStateFlow.testIn(this).awaitItemAndCancel()
            val actualLunarPhaseDetails = lunarPhaseDetailsRepo.findLunarPhaseDetails(instant, city)

            val lunarPhase = (lunarPhaseDetails as State.Success).value.lunarPhase
            assertThat(lunarPhase).isEqualTo(actualLunarPhaseDetails.lunarPhase)
        }
    }

    @Test
    fun getUpcomingLunarPhaseStateFlow() = runTest {
        val instants = getConsecutiveInstants(max = 1)

        var lunarPhaseDetails = lunarPhaseDetailsRepo.lunarPhaseDetailsStateFlow.testIn(this).awaitItemAndCancel()
        assertThat(lunarPhaseDetails).isInstanceOf(State.Error::class.java)

        instants.forEach { instant ->
            lunarPhaseDetailsRepo.refreshLunarPhaseDetails(instant, city)
            lunarPhaseDetails = lunarPhaseDetailsRepo.lunarPhaseDetailsStateFlow.testIn(this).awaitItemAndCancel()
            val actualLunarPhaseDetails = lunarPhaseDetailsRepo.findLunarPhaseDetails(instant, city)

            val lunarPhase = (lunarPhaseDetails as State.Success).value.lunarPhase
            assertThat(lunarPhase).isEqualTo(actualLunarPhaseDetails.lunarPhase)
        }
    }
}
