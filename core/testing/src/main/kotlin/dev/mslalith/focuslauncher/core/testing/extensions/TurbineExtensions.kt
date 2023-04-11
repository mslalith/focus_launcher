package dev.mslalith.focuslauncher.core.testing.extensions

import app.cash.turbine.testIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

context (CoroutineScope)
suspend fun <T> Flow<T>.awaitItem(): T {
    val turbine = testIn(scope = this@CoroutineScope)
    val item = turbine.awaitItem()
    turbine.cancel()
    return item
}

context (CoroutineScope)
suspend fun <T> Flow<T>.awaitItemChangeUntil(
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

context (CoroutineScope)
suspend fun <T, R> Flow<T>.awaitItemChange(
    valueFor: (T) -> R
): R {
    val turbine = testIn(scope = this@CoroutineScope)
    val lastItem = valueFor(turbine.expectMostRecentItem())

    var item = valueFor(turbine.awaitItem())
    while (lastItem == item) {
        item = valueFor(turbine.awaitItem())
    }

    turbine.cancel()
    return item
}
