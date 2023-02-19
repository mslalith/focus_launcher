package dev.mslalith.focuslauncher.core.common.extensions

import android.os.Handler
import android.os.Looper
import kotlinx.datetime.LocalDateTime
import java.time.ZonedDateTime
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
