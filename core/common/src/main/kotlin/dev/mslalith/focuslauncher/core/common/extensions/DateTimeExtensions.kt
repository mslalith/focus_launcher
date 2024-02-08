package dev.mslalith.focuslauncher.core.common.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.Month
import java.time.ZonedDateTime
import java.util.Locale

fun ZonedDateTime.inShortReadableFormat(): String {
    val (_, month, day) = this.toString().substringBefore(delimiter = "T").split("-")
    val monthReadable = Month.entries[month.toInt() - 1].toString()
        .lowercase(locale = Locale.getDefault())
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale = Locale.getDefault()) else it.toString() }

    val daySuffix = when (day.toInt()) {
        1, 21, 31 -> "st"
        2, 22 -> "nd"
        3, 23 -> "rd"
        else -> "th"
    }
    return "$day$daySuffix $monthReadable"
}

fun LocalDateTime.inShortReadableFormat(
    shortMonthName: Boolean = false
): String {
    val daySuffix = when (dayOfMonth) {
        1, 21, 31 -> "st"
        2, 22 -> "nd"
        3, 23 -> "rd"
        else -> "th"
    }
    val monthReadable =
        month.toString()
            .lowercase(locale = Locale.getDefault())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            .run { if (shortMonthName) take(n = 3) else this }

    return "$dayOfMonth$daySuffix $monthReadable"
}

fun ZonedDateTime.toKotlinxLocalDateTime(): LocalDateTime? = try {
    val isoString = toLocalDateTime().toString().substringBefore(delimiter = "+")
    LocalDateTime.parse(isoString)
} catch (ex: IllegalArgumentException) {
    null
}

fun Instant.formatToTime(use24Hour: Boolean): String {
    return toLocalDateTime(TimeZone.currentSystemDefault()).run {
        val h = if (use24Hour) hour else when {
            hour >= 12 -> hour - 12
            else -> hour
        }
        listOf(h, minute)
            .map { it.to2Digit() }
            .joinToString(separator = ":")
    }
}
