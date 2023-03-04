package dev.mslalith.focuslauncher.core.data.repository

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.extensions.limitDecimals
import dev.mslalith.focuslauncher.core.common.getOrNull
import dev.mslalith.focuslauncher.core.model.City
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhase
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.NextPhaseDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.RiseAndSetDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.UpcomingLunarPhase
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import java.time.Month
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@Ignore
internal class LunarPhaseDetailsRepoTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: LunarPhaseDetailsRepo

    private val city = City(id = 0, name = "Something", latitude = 0.0, longitude = 0.0)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun `given full moon date, verify phase is full moon`() = runCoroutineTest {
        val instant = instantOf(dayOfMonth = 5, hour = 18, minute = 30)
        repo.refreshLunarPhaseDetails(instant = instant, city = city)

        val expectedLunarPhaseDetails = LunarPhaseDetails(
            lunarPhase = LunarPhase.FULL_MOON,
            illumination = 0.9981534868284623,
            phaseAngle = 4.9256389938282545,
            nextPhaseDetails = NextPhaseDetails(
                newMoon = LocalDateTime.parse("2023-02-20T12:38:17.496"),
                fullMoon = LocalDateTime.parse("2023-03-07T18:09:47.981"),
            ),
            moonRiseAndSetDetails = RiseAndSetDetails(
                riseDateTime = LocalDateTime.parse("2023-02-07T00:34:47"),
                setDateTime = LocalDateTime.parse("2023-02-06T12:12:09")
            ),
            sunRiseAndSetDetails = RiseAndSetDetails(
                riseDateTime = LocalDateTime.parse("2023-02-06T11:40:34"),
                setDateTime = LocalDateTime.parse("2023-02-06T23:47:36")
            )
        )
        val expectedUpcomingLunarPhase = UpcomingLunarPhase(
            lunarPhase = LunarPhase.NEW_MOON,
            dateTime = LocalDateTime.parse("2023-02-20T12:38:17.496"),
            isMicroMoon = false,
            isSuperMoon = true
        )

        repo.assertLunarPhaseDetailsWith(expectedLunarPhaseDetails)
        repo.assertUpcomingLunarPhaseWith(expectedUpcomingLunarPhase)
    }

    @Test
    fun `given new moon date, verify phase is new moon`() = runCoroutineTest {
        val instant = instantOf(dayOfMonth = 20, hour = 7, minute = 9)
        repo.refreshLunarPhaseDetails(instant = instant, city = city)

        val expectedLunarPhaseDetails = LunarPhaseDetails(
            lunarPhase = LunarPhase.NEW_MOON,
            illumination = 0.001589384074297484,
            phaseAngle = -175.43035810796886,
            nextPhaseDetails = NextPhaseDetails(
                newMoon = LocalDateTime.parse("2023-03-21T22:55:46.770"),
                fullMoon = LocalDateTime.parse("2023-03-07T18:09:47.981"),
            ),
            moonRiseAndSetDetails = RiseAndSetDetails(
                riseDateTime = LocalDateTime.parse("2023-02-21T12:43:01"),
                setDateTime = LocalDateTime.parse("2023-02-21T00:15:18")
            ),
            sunRiseAndSetDetails = RiseAndSetDetails(
                riseDateTime = LocalDateTime.parse("2023-02-21T11:40:15"),
                setDateTime = LocalDateTime.parse("2023-02-20T23:47:10")
            )
        )
        val expectedUpcomingLunarPhase = UpcomingLunarPhase(
            lunarPhase = LunarPhase.FULL_MOON,
            dateTime = LocalDateTime.parse("2023-03-07T18:09:47.981"),
            isMicroMoon = false,
            isSuperMoon = false
        )

        repo.assertLunarPhaseDetailsWith(expectedLunarPhaseDetails)
        repo.assertUpcomingLunarPhaseWith(expectedUpcomingLunarPhase)
    }

    @Test
    fun `given first quarter date, verify phase is first quarter`() = runCoroutineTest {
        val instant = instantOf(dayOfMonth = 27, hour = 8, minute = 6)
        repo.refreshLunarPhaseDetails(instant = instant, city = city)

        val expectedLunarPhaseDetails = LunarPhaseDetails(
            lunarPhase = LunarPhase.FIRST_QUARTER,
            illumination = 0.499714382113712,
            phaseAngle = -90.03272940065548,
            nextPhaseDetails = NextPhaseDetails(
                newMoon = LocalDateTime.parse("2023-03-21T22:55:46.771"),
                fullMoon = LocalDateTime.parse("2023-03-07T18:09:47.981"),
            ),
            moonRiseAndSetDetails = RiseAndSetDetails(
                riseDateTime = LocalDateTime.parse("2023-02-27T17:35:51"),
                setDateTime = LocalDateTime.parse("2023-02-28T06:00:33")
            ),
            sunRiseAndSetDetails = RiseAndSetDetails(
                riseDateTime = LocalDateTime.parse("2023-02-28T11:39:14"),
                setDateTime = LocalDateTime.parse("2023-02-27T23:46:07")
            )
        )
        val expectedUpcomingLunarPhase = UpcomingLunarPhase(
            lunarPhase = LunarPhase.FULL_MOON,
            dateTime = LocalDateTime.parse("2023-03-07T18:09:47.981"),
            isMicroMoon = false,
            isSuperMoon = false
        )

        repo.assertLunarPhaseDetailsWith(expectedLunarPhaseDetails)
        repo.assertUpcomingLunarPhaseWith(expectedUpcomingLunarPhase)
    }

    @Test
    fun `given last quarter date, verify phase is last quarter`() = runCoroutineTest {
        val instant = instantOf(dayOfMonth = 13, hour = 4, minute = 3)
        repo.refreshLunarPhaseDetails(instant = instant, city = city)

        val expectedLunarPhaseDetails = LunarPhaseDetails(
            lunarPhase = LunarPhase.LAST_QUARTER,
            illumination = 0.5538529537384493,
            phaseAngle = 83.81691199458245,
            nextPhaseDetails = NextPhaseDetails(
                newMoon = LocalDateTime.parse("2023-02-20T12:38:17.496"),
                fullMoon = LocalDateTime.parse("2023-03-07T18:09:47.981"),
            ),
            moonRiseAndSetDetails = RiseAndSetDetails(
                riseDateTime = LocalDateTime.parse("2023-02-14T05:41:42"),
                setDateTime = LocalDateTime.parse("2023-02-13T17:14:22")
            ),
            sunRiseAndSetDetails = RiseAndSetDetails(
                riseDateTime = LocalDateTime.parse("2023-02-13T11:40:46"),
                setDateTime = LocalDateTime.parse("2023-02-13T23:47:40")
            )
        )
        val expectedUpcomingLunarPhase = UpcomingLunarPhase(
            lunarPhase = LunarPhase.NEW_MOON,
            dateTime = LocalDateTime.parse("2023-02-20T12:38:17.496"),
            isMicroMoon = false,
            isSuperMoon = true
        )

        repo.assertLunarPhaseDetailsWith(expectedLunarPhaseDetails)
        repo.assertUpcomingLunarPhaseWith(expectedUpcomingLunarPhase)
    }
}

private fun instantOf(dayOfMonth: Int, hour: Int, minute: Int) = LocalDateTime(
    year = 2023,
    month = Month.FEBRUARY,
    dayOfMonth = dayOfMonth,
    hour = hour,
    minute = minute,
    second = 0,
    nanosecond = 0
).toInstant(timeZone = TimeZone.UTC)

context (CoroutineScope)
private suspend fun LunarPhaseDetailsRepo.assertLunarPhaseDetailsWith(expected: LunarPhaseDetails) {
    val lunarPhaseDetails = lunarPhaseDetailsStateFlow.awaitItem().getOrNull()
    assertThat(lunarPhaseDetails?.lunarPhase).isEqualTo(expected.lunarPhase)
    assertThat(lunarPhaseDetails?.illumination?.limitDecimals(precision = 5)).isEqualTo(expected.illumination.limitDecimals(precision = 5))
    assertThat(lunarPhaseDetails?.phaseAngle?.limitDecimals(precision = 5)).isEqualTo(expected.phaseAngle.limitDecimals(precision = 5))
    assertLocalDateTime(lunarPhaseDetails?.nextPhaseDetails?.fullMoon, expected.nextPhaseDetails.fullMoon)
    assertLocalDateTime(lunarPhaseDetails?.nextPhaseDetails?.newMoon, expected.nextPhaseDetails.newMoon)
    assertLocalDateTime(lunarPhaseDetails?.moonRiseAndSetDetails?.riseDateTime, expected.moonRiseAndSetDetails.riseDateTime)
    assertLocalDateTime(lunarPhaseDetails?.moonRiseAndSetDetails?.setDateTime, expected.moonRiseAndSetDetails.setDateTime)
    assertLocalDateTime(lunarPhaseDetails?.sunRiseAndSetDetails?.riseDateTime, expected.sunRiseAndSetDetails.riseDateTime)
    assertLocalDateTime(lunarPhaseDetails?.sunRiseAndSetDetails?.setDateTime, expected.sunRiseAndSetDetails.setDateTime)
}

context (CoroutineScope)
private suspend fun LunarPhaseDetailsRepo.assertUpcomingLunarPhaseWith(expected: UpcomingLunarPhase) {
    val upcomingLunarPhase = upcomingLunarPhaseStateFlow.awaitItem().getOrNull()
    assertThat(upcomingLunarPhase?.lunarPhase).isEqualTo(expected.lunarPhase)
    assertLocalDateTime(upcomingLunarPhase?.dateTime, expected.dateTime)
    assertThat(upcomingLunarPhase?.isMicroMoon).isEqualTo(expected.isMicroMoon)
    assertThat(upcomingLunarPhase?.isSuperMoon).isEqualTo(expected.isSuperMoon)
}

private fun assertLocalDateTime(actual: LocalDateTime?, expected: LocalDateTime?) {
    assertThat(actual?.year).isEqualTo(expected?.year)
    assertThat(actual?.month).isEqualTo(expected?.month)
    assertThat(actual?.dayOfMonth).isEqualTo(expected?.dayOfMonth)
    assertThat(actual?.hour).isEqualTo(expected?.hour)
    assertThat(actual?.minute).isEqualTo(expected?.minute)
    assertThat(actual?.second).isEqualTo(expected?.second)
}
