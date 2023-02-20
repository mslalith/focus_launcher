package dev.mslalith.focuslauncher.core.common.extensions

import android.os.Handler
import android.os.Looper

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

fun Double.asPercent(precision: Int = 2) = "%.${precision}f".format(this) + "%"
fun Double.limitDecimals(precision: Int = 2) = "%.${precision}f".format(this)

fun Char.isAlphabet() = when (code) {
    in 65..90 -> true
    in 97..122 -> true
    else -> false
}
