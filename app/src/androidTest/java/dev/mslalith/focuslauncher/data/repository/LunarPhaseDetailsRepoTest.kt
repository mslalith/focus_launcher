package dev.mslalith.focuslauncher.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.data.model.Outcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class LunarPhaseDetailsRepoTest {

    private lateinit var clockRepo: ClockRepo
    private lateinit var lunarPhaseDetailsRepo: LunarPhaseDetailsRepo


    @Before
    fun setUp() {
        clockRepo = ClockRepo()
        lunarPhaseDetailsRepo = LunarPhaseDetailsRepo(clockRepo = clockRepo)
    }

    @After
    fun tearDown() {
    }

    private fun getConsecutiveInstants(max: Int): List<Instant> = buildList {
        val instant = Clock.System.now()
        add(instant)
        (1..max).forEach { index ->
            val duration = index.seconds
            add(instant.plus(duration))
        }
    }

    @Test
    fun getLunarPhaseDetailsStateFlow() = runTest {
        val instants = getConsecutiveInstants(max = 2)

        val job = launch {
            lunarPhaseDetailsRepo.lunarPhaseDetailsStateFlow.test {
                assertThat(awaitItem()).isInstanceOf(Outcome.Error::class.java)
                instants.forEach { instant ->
                    val lunarPhaseDetails = lunarPhaseDetailsRepo.findLunarPhaseDetails(instant)
                    val lunarPhase = (awaitItem() as Outcome.Success).value.lunarPhase
                    assertThat(lunarPhase).isEqualTo(lunarPhaseDetails.lunarPhase)
                }
                expectNoEvents()
            }
        }

        instants.forEach { instant ->
            delay(1_000)
            clockRepo.refreshTime(instant)
        }
        job.join()
    }

    @Test
    fun getUpcomingLunarPhaseStateFlow() = runTest {
        val instants = getConsecutiveInstants(max = 6)

        val job = launch {
            lunarPhaseDetailsRepo.upcomingLunarPhaseStateFlow.test {
                assertThat(awaitItem()).isInstanceOf(Outcome.Error::class.java)
                instants.forEach { instant ->
                    val lunarPhaseDetails = lunarPhaseDetailsRepo.findLunarPhaseDetails(instant)
                    val upcomingLunarPhase = lunarPhaseDetailsRepo.findUpcomingMoonPhaseFor(lunarPhaseDetails.direction)
                    val lunarPhase = (awaitItem() as Outcome.Success).value.lunarPhase
                    assertThat(lunarPhase).isEqualTo(upcomingLunarPhase.lunarPhase)
                }
                expectNoEvents()
            }
        }

        instants.forEach { instant ->
            delay(1_000)
            clockRepo.refreshTime(instant)
        }
        job.join()
    }
}
