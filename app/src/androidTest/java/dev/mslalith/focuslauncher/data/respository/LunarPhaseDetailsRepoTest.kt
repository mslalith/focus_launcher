package dev.mslalith.focuslauncher.data.respository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.FakeLunarPhaseDetailsRepo
import dev.mslalith.focuslauncher.data.models.Outcome
import dev.mslalith.focuslauncher.extensions.formatToTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class LunarPhaseDetailsRepoTest {

    private lateinit var context: Context
    private lateinit var lunarPhaseDetailsRepo: FakeLunarPhaseDetailsRepo

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        lunarPhaseDetailsRepo = FakeLunarPhaseDetailsRepo(TestCoroutineScope())
    }

    @After
    fun tearDown() {
    }

    private fun getConsecutiveInstants(max: Int): List<Instant> {
        return buildList {
            val instant = Clock.System.now()
            add(instant)
            (1..max).forEach { index ->
                val duration = index.seconds
                add(instant.plus(duration))
            }
        }
    }

    @Test
    fun getCurrentTimeStateFlow() = runTest {
        val instants = getConsecutiveInstants(max = 2)
        lunarPhaseDetailsRepo.instantTimeList = instants

        val job = launch {
            lunarPhaseDetailsRepo.currentTimeStateFlow.test {
                instants.forEach { instant ->
                    val time = instant.formatToTime()
                    val expectedTime = (awaitItem() as Outcome.Success).value
                    assertThat(expectedTime).isEqualTo(time)
                }
                expectNoEvents()
            }
        }

        lunarPhaseDetailsRepo.registerToTimeChange(context)
        job.join()
    }

    @Test
    fun getLunarPhaseDetailsStateFlow() = runTest {
        val instants = getConsecutiveInstants(max = 5)
        lunarPhaseDetailsRepo.instantTimeList = instants

        val job = launch {
            lunarPhaseDetailsRepo.lunarPhaseDetailsStateFlow.test {
                instants.forEach { instant ->
                    val lunarPhaseDetails = lunarPhaseDetailsRepo.findLunarPhaseDetails(instant)
                    val lunarPhase = (awaitItem() as Outcome.Success).value.lunarPhase
                    assertThat(lunarPhase).isEqualTo(lunarPhaseDetails.lunarPhase)
                }
                expectNoEvents()
            }
        }

        lunarPhaseDetailsRepo.registerToTimeChange(context)
        job.join()
    }

    @Test
    fun getUpcomingLunarPhaseStateFlow() = runTest {
        val instants = getConsecutiveInstants(max = 5)
        lunarPhaseDetailsRepo.instantTimeList = instants

        val job = launch {
            lunarPhaseDetailsRepo.upcomingLunarPhaseStateFlow.test {
                instants.forEach { instant ->
                    val lunarPhaseDetails = lunarPhaseDetailsRepo.findLunarPhaseDetails(instant)
                    val upcomingLunarPhase = lunarPhaseDetailsRepo.findUpcomingMoonPhaseFor(lunarPhaseDetails.direction)
                    val lunarPhase = (awaitItem() as Outcome.Success).value.lunarPhase
                    assertThat(lunarPhase).isEqualTo(upcomingLunarPhase.lunarPhase)
                }
                expectNoEvents()
            }
        }

        lunarPhaseDetailsRepo.registerToTimeChange(context)
        job.join()
    }

    @Test
    fun registerToTimeChange() = runTest {
        val job = launch {
            lunarPhaseDetailsRepo.isTimeChangeBroadcastReceiverRegisteredStateFlow.test {
                assertThat(awaitItem()).isFalse()
                assertThat(awaitItem()).isTrue()
                assertThat(awaitItem()).isFalse()
                expectNoEvents()
            }
        }

        lunarPhaseDetailsRepo.registerToTimeChange(context)
        advanceTimeBy(2_000)
        lunarPhaseDetailsRepo.unregisterToTimeChange(context)

        job.join()
    }

    @Test
    fun unregisterToTimeChange() = runTest {
        val job = launch {
            lunarPhaseDetailsRepo.isTimeChangeBroadcastReceiverRegisteredStateFlow.test {
                assertThat(awaitItem()).isFalse()
                assertThat(awaitItem()).isTrue()
                assertThat(awaitItem()).isFalse()
                expectNoEvents()
            }
        }

        lunarPhaseDetailsRepo.registerToTimeChange(context)
        advanceTimeBy(3_000)
        lunarPhaseDetailsRepo.unregisterToTimeChange(context)

        job.join()
    }
}
