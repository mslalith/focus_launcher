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
