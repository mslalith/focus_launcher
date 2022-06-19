package dev.mslalith.focuslauncher.data.extensions

import android.os.Handler
import android.os.Looper
import kotlinx.datetime.LocalDateTime
import java.time.ZonedDateTime

fun runAfter(delayMillis: Long, action: () -> Unit) = Handler(Looper.getMainLooper()).postDelayed(action, delayMillis)

fun waitAndRunAfter(
    startTime: Long,
    minTime: Long = 2000L,
    action: () -> Unit,
) {
    val elapsedTime = System.currentTimeMillis() - startTime
    val waitTimeMillis = if (elapsedTime < minTime) minTime - elapsedTime else 0
    runAfter(
        delayMillis = waitTimeMillis,
        action = action,
    )
}

fun ZonedDateTime.toKotlinxLocalDateTime() = try {
    val isoString = this.toString().substringBefore(delimiter = "+")
    LocalDateTime.parse(isoString)
} catch (ex: Exception) {
    null
}
