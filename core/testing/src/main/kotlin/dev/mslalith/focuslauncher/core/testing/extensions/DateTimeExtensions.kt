package dev.mslalith.focuslauncher.core.testing.extensions

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun instantOf(
    year: Int = 2023,
    month: Month = Month.FEBRUARY,
    dayOfMonth: Int = 12,
    hour: Int,
    minute: Int
): Instant = LocalDateTime(
    year = year,
    month = month,
    day = dayOfMonth,
    hour = hour,
    minute = minute,
    second = 0,
    nanosecond = 0
).toInstant(timeZone = TimeZone.UTC)

inline fun withTimeZone(
    timeZone: String,
    block: () -> Unit
) {
    val original = java.util.TimeZone.getDefault()
    java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone(timeZone))
    block()
    java.util.TimeZone.setDefault(original)
}

inline fun withUtcTimeZone(
    block: () -> Unit
) = withTimeZone(
    timeZone = "UTC",
    block = block
)
