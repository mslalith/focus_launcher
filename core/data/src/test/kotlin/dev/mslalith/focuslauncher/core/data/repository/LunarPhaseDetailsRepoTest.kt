package dev.mslalith.focuslauncher.core.data.repository

import app.cash.turbine.TurbineContext
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.extensions.limitDecimals
import dev.mslalith.focuslauncher.core.common.model.getOrNull
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhase
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.NextPhaseDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.RiseAndSetDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.UpcomingLunarPhase
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import dev.mslalith.focuslauncher.core.testing.extensions.instantOf
import dev.mslalith.focuslauncher.core.testing.extensions.withUtcTimeZone
import javax.inject.Inject
import kotlinx.datetime.LocalDateTime
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(AppRobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
internal class LunarPhaseDetailsRepoTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: LunarPhaseDetailsRepo

    private val latLngZero = LatLng(latitude = 0.0, longitude = 0.0)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun `given full moon date, verify phase is full moon`() = runCoroutineTest {
        withUtcTimeZone {
            val instant = instantOf(dayOfMonth = 5, hour = 18, minute = 30)
            repo.refreshLunarPhaseDetails(instant = instant, latLng = latLngZero)

            val expectedLunarPhaseDetails = LunarPhaseDetails(
                lunarPhase = LunarPhase.FULL_MOON,
                illumination = 0.9981534868284623,
                phaseAngle = 4.9256389938282545,
                nextPhaseDetails = NextPhaseDetails(
                    newMoon = LocalDateTime.parse("2023-02-20T07:08:17.496"),
                    fullMoon = LocalDateTime.parse("2023-03-07T12:39:47.981")
                ),
                moonRiseAndSetDetails = RiseAndSetDetails(
                    riseDateTime = LocalDateTime.parse("2023-02-05T18:20:24"),
                    setDateTime = LocalDateTime.parse("2023-02-05T05:56:36")
                ),
                sunRiseAndSetDetails = RiseAndSetDetails(
                    riseDateTime = LocalDateTime.parse("2023-02-05T06:10:28"),
                    setDateTime = LocalDateTime.parse("2023-02-05T18:17:32")
                )
            )
            val expectedUpcomingLunarPhase = UpcomingLunarPhase(
                lunarPhase = LunarPhase.NEW_MOON,
                dateTime = LocalDateTime.parse("2023-02-20T07:08:17.496"),
                isMicroMoon = false,
                isSuperMoon = true
            )

            repo.assertLunarPhaseDetailsWith(expected = expectedLunarPhaseDetails)
            repo.assertUpcomingLunarPhaseWith(expected = expectedUpcomingLunarPhase)
        }
    }

    @Test
    fun `given new moon date, verify phase is new moon`() = runCoroutineTest {
        withUtcTimeZone {
            val instant = instantOf(dayOfMonth = 20, hour = 7, minute = 9)
            repo.refreshLunarPhaseDetails(instant = instant, latLng = latLngZero)

            val expectedLunarPhaseDetails = LunarPhaseDetails(
                lunarPhase = LunarPhase.NEW_MOON,
                illumination = 0.001589384074297484,
                phaseAngle = -175.43035810796886,
                nextPhaseDetails = NextPhaseDetails(
                    newMoon = LocalDateTime.parse("2023-03-21T17:25:46.770"),
                    fullMoon = LocalDateTime.parse("2023-03-07T12:39:47.982")
                ),
                moonRiseAndSetDetails = RiseAndSetDetails(
                    riseDateTime = LocalDateTime.parse("2023-02-20T06:19:22"),
                    setDateTime = LocalDateTime.parse("2023-02-20T18:45:18")
                ),
                sunRiseAndSetDetails = RiseAndSetDetails(
                    riseDateTime = LocalDateTime.parse("2023-02-20T06:10:21"),
                    setDateTime = LocalDateTime.parse("2023-02-20T18:17:10")
                )
            )
            val expectedUpcomingLunarPhase = UpcomingLunarPhase(
                lunarPhase = LunarPhase.FULL_MOON,
                dateTime = LocalDateTime.parse("2023-03-07T12:39:47.982"),
                isMicroMoon = false,
                isSuperMoon = false
            )

            repo.assertLunarPhaseDetailsWith(expected = expectedLunarPhaseDetails)
            repo.assertUpcomingLunarPhaseWith(expected = expectedUpcomingLunarPhase)
        }
    }

    @Test
    fun `given first quarter date, verify phase is first quarter`() = runCoroutineTest {
        withUtcTimeZone {
            val instant = instantOf(dayOfMonth = 27, hour = 8, minute = 6)
            repo.refreshLunarPhaseDetails(instant = instant, latLng = latLngZero)

            val expectedLunarPhaseDetails = LunarPhaseDetails(
                lunarPhase = LunarPhase.FIRST_QUARTER,
                illumination = 0.499714382113712,
                phaseAngle = -90.03272940065548,
                nextPhaseDetails = NextPhaseDetails(
                    newMoon = LocalDateTime.parse("2023-03-21T17:25:46.771"),
                    fullMoon = LocalDateTime.parse("2023-03-07T12:39:47.981")
                ),
                moonRiseAndSetDetails = RiseAndSetDetails(
                    riseDateTime = LocalDateTime.parse("2023-02-27T12:05:52"),
                    setDateTime = LocalDateTime.parse("2023-02-28T00:30:33")
                ),
                sunRiseAndSetDetails = RiseAndSetDetails(
                    riseDateTime = LocalDateTime.parse("2023-02-27T06:09:24"),
                    setDateTime = LocalDateTime.parse("2023-02-27T18:16:07")
                )
            )
            val expectedUpcomingLunarPhase = UpcomingLunarPhase(
                lunarPhase = LunarPhase.FULL_MOON,
                dateTime = LocalDateTime.parse("2023-03-07T12:39:47.981"),
                isMicroMoon = false,
                isSuperMoon = false
            )

            repo.assertLunarPhaseDetailsWith(expected = expectedLunarPhaseDetails)
            repo.assertUpcomingLunarPhaseWith(expected = expectedUpcomingLunarPhase)
        }
    }

    @Test
    fun `given last quarter date, verify phase is last quarter`() = runCoroutineTest {
        withUtcTimeZone {
            val instant = instantOf(dayOfMonth = 13, hour = 4, minute = 3)
            repo.refreshLunarPhaseDetails(instant = instant, latLng = latLngZero)

            val expectedLunarPhaseDetails = LunarPhaseDetails(
                lunarPhase = LunarPhase.LAST_QUARTER,
                illumination = 0.5538529537384493,
                phaseAngle = 83.81691199458245,
                nextPhaseDetails = NextPhaseDetails(
                    newMoon = LocalDateTime.parse("2023-02-20T07:08:17.492"),
                    fullMoon = LocalDateTime.parse("2023-03-07T12:39:47.981")
                ),
                moonRiseAndSetDetails = RiseAndSetDetails(
                    riseDateTime = LocalDateTime.parse("2023-02-14T00:11:42"),
                    setDateTime = LocalDateTime.parse("2023-02-13T11:44:22")
                ),
                sunRiseAndSetDetails = RiseAndSetDetails(
                    riseDateTime = LocalDateTime.parse("2023-02-13T06:10:46"),
                    setDateTime = LocalDateTime.parse("2023-02-13T18:17:41")
                )
            )
            val expectedUpcomingLunarPhase = UpcomingLunarPhase(
                lunarPhase = LunarPhase.NEW_MOON,
                dateTime = LocalDateTime.parse("2023-02-20T07:08:17.492"),
                isMicroMoon = false,
                isSuperMoon = true
            )

            repo.assertLunarPhaseDetailsWith(expected = expectedLunarPhaseDetails)
            repo.assertUpcomingLunarPhaseWith(expected = expectedUpcomingLunarPhase)
        }
    }
}

context (TurbineContext)
private suspend fun LunarPhaseDetailsRepo.assertLunarPhaseDetailsWith(expected: LunarPhaseDetails) {
    val lunarPhaseDetails = lunarPhaseDetailsStateFlow.awaitItem().getOrNull()
    assertThat(lunarPhaseDetails?.lunarPhase).isEqualTo(expected.lunarPhase)
    assertThat(lunarPhaseDetails?.illumination?.limitDecimals(maxFractions = 5)).isEqualTo(expected.illumination.limitDecimals(maxFractions = 5))
    assertThat(lunarPhaseDetails?.phaseAngle?.limitDecimals(maxFractions = 5)).isEqualTo(expected.phaseAngle.limitDecimals(maxFractions = 5))
    assertLocalDateTime(lunarPhaseDetails?.nextPhaseDetails?.fullMoon, expected.nextPhaseDetails.fullMoon)
    assertLocalDateTime(lunarPhaseDetails?.nextPhaseDetails?.newMoon, expected.nextPhaseDetails.newMoon)
    assertLocalDateTime(lunarPhaseDetails?.moonRiseAndSetDetails?.riseDateTime, expected.moonRiseAndSetDetails.riseDateTime)
    assertLocalDateTime(lunarPhaseDetails?.moonRiseAndSetDetails?.setDateTime, expected.moonRiseAndSetDetails.setDateTime)
    assertLocalDateTime(lunarPhaseDetails?.sunRiseAndSetDetails?.riseDateTime, expected.sunRiseAndSetDetails.riseDateTime)
    assertLocalDateTime(lunarPhaseDetails?.sunRiseAndSetDetails?.setDateTime, expected.sunRiseAndSetDetails.setDateTime)
}

context (TurbineContext)
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
