package dev.mslalith.focuslauncher.core.testing.extensions

import app.cash.turbine.testIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

context (CoroutineScope)
suspend inline fun <T> Flow<T>.awaitItem(): T {
    val turbine = testIn(scope = this@CoroutineScope)
    val item = turbine.awaitItem()
    turbine.cancel()
    return item
}

context (CoroutineScope)
suspend inline fun <T> Flow<T>.awaitItemChangeUntil(
    awaitTill: (T) -> Boolean
): T {
    val turbine = testIn(scope = this@CoroutineScope)
    var lastItem = turbine.expectMostRecentItem()

    while (!awaitTill(lastItem)) {
        lastItem = turbine.awaitItem()
    }

    turbine.cancel()
    return lastItem
}
