package dev.mslalith.focuslauncher.core.testing.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

fun instantOf(
    year: Int = 2023,
    month: Month = Month.FEBRUARY,
    dayOfMonth: Int = 12,
    hour: Int,
    minute: Int
): Instant = LocalDateTime(
    year = year,
    month = month,
    dayOfMonth = dayOfMonth,
    hour = hour,
    minute = minute,
    second = 0,
    nanosecond = 0
).toInstant(timeZone = TimeZone.UTC)

inline fun withTimeZone(
    timeZone: String = "UTC",
    block: () -> Unit
) {
    val original = java.util.TimeZone.getDefault()
    java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone(timeZone))
    block()
    java.util.TimeZone.setDefault(original)
}
