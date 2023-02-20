package dev.mslalith.focuslauncher.extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.Month
import java.time.ZonedDateTime
import java.util.Locale

fun ZonedDateTime.isSameAsToday(): Boolean {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val (year, month, day) = this.toString().substringBefore("T").split("-")
    return today.year == year.toInt() && today.monthNumber == month.toInt() && today.dayOfMonth == day.toInt()
}

fun ZonedDateTime.inShortReadableFormat(): String {
    val (_, month, day) = this.toString().substringBefore("T").split("-")
    val monthReadable = Month.values()[month.toInt() - 1].toString()
        .lowercase(Locale.getDefault())
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

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
            .lowercase(Locale.getDefault())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            .run { if (shortMonthName) take(3) else this }

    return "$dayOfMonth$daySuffix $monthReadable"
}
