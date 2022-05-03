package dev.mslalith.focuslauncher.data.repository

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.data.network.repository.ClockRepo
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class ClockRepoTest {

    private lateinit var clockRepo: ClockRepo

    @Before
    fun setUp() {
        clockRepo = ClockRepo()
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
    fun getCurrentInstantStateFlow() {
        val instants = getConsecutiveInstants(max = 2)
        instants.forEach { instant ->
            clockRepo.refreshTime(instant)
            assertThat(clockRepo.currentInstantStateFlow.value).isEqualTo(instant)
        }
    }

    @Test
    fun refreshTime() {
        val instants = getConsecutiveInstants(max = 6)
        instants.forEach { instant ->
            clockRepo.refreshTime(instant)
            assertThat(clockRepo.currentInstantStateFlow.value).isEqualTo(instant)
        }
    }
}
