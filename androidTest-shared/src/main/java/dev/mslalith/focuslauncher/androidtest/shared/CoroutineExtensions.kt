package dev.mslalith.focuslauncher.androidtest.shared

import app.cash.turbine.FlowTurbine

suspend fun <T> FlowTurbine<T>.awaitItemAndCancel(): T {
    val item = awaitItem()
    cancel()
    return item
}
