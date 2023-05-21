package dev.mslalith.focuslauncher.core.data.repository

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.providers.clock.test.TestClockProvider
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import dev.mslalith.focuslauncher.core.testing.extensions.instantOf
import javax.inject.Inject
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
internal class ClockRepoTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var clockProvider: TestClockProvider

    @Inject
    lateinit var repo: ClockRepo

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun `when refreshed, the clock time should be updated`() = runCoroutineTest {
        clockProvider.setInstant(instant = instantOf(hour = 3, minute = 44))
        repo.refreshTime()

        val expected = LocalDateTime.parse("2023-02-12T03:44:00.000").toInstant(TimeZone.UTC)
        assertThat(repo.currentInstantStateFlow.awaitItem()).isEqualTo(expected)
    }
}
