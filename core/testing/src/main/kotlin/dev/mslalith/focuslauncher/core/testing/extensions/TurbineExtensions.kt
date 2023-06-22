package dev.mslalith.focuslauncher.core.testing.extensions

import app.cash.turbine.TurbineContext
import kotlinx.coroutines.flow.Flow

context (TurbineContext)
suspend fun <T> Flow<T>.awaitItem(): T {
    val turbine = testIn(scope = this@TurbineContext)
    val item = turbine.awaitItem()
    turbine.cancel()
    return item
}

context (TurbineContext)
suspend fun <T> Flow<T>.awaitItemChangeUntil(
    awaitTill: (T) -> Boolean
): T {
    val turbine = testIn(scope = this@TurbineContext)
    var lastItem = turbine.expectMostRecentItem()

    while (!awaitTill(lastItem)) {
        lastItem = turbine.awaitItem()
    }

    turbine.cancel()
    return lastItem
}
