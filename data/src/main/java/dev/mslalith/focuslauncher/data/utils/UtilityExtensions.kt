package dev.mslalith.focuslauncher.data.utils

import android.os.Handler
import android.os.Looper

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
