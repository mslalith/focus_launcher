package dev.mslalith.focuslauncher.core.common.extensions

import android.os.Handler
import android.os.Looper
import kotlinx.datetime.LocalDateTime
import java.time.ZonedDateTime
import java.util.Locale
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.formatToTime(): String {
    return toLocalDateTime(TimeZone.currentSystemDefault()).run {
        listOf(hour, minute).map { it.to2Digit() }.joinToString(separator = ":")
    }
}

fun Int.to2Digit() = when {
    this < 10 -> "0$this"
    else -> this
}

fun runAfter(delayMillis: Long, action: () -> Unit) = Handler(Looper.getMainLooper()).postDelayed(action, delayMillis)

fun waitAndRunAfter(
    startTime: Long,
    minTime: Long = 2000L,
    action: () -> Unit
) {
    val elapsedTime = System.currentTimeMillis() - startTime
    val waitTimeMillis = if (elapsedTime < minTime) minTime - elapsedTime else 0
    runAfter(
        delayMillis = waitTimeMillis,
        action = action
    )
}

fun ZonedDateTime.toKotlinxLocalDateTime() = try {
    val isoString = this.toString().substringBefore(delimiter = "+")
    LocalDateTime.parse(isoString)
} catch (ex: Exception) {
    null
}

fun Double.asPercent(precision: Int = 2) = "%.${precision}f".format(this) + "%"
fun Double.limitDecimals(precision: Int = 2) = "%.${precision}f".format(this)

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

fun Char.isAlphabet() = when (code) {
    in 65..90 -> true
    in 97..122 -> true
    else -> false
}
