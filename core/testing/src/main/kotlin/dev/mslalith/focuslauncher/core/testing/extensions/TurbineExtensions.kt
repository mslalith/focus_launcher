package dev.mslalith.focuslauncher.core.testing.extensions

import app.cash.turbine.ReceiveTurbine

suspend fun <T> ReceiveTurbine<T>.awaitItemAndCancel(): T {
    val item = awaitItem()
    cancel()
    return item
}
