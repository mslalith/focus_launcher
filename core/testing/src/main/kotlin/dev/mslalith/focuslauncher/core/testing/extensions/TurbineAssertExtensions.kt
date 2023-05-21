package dev.mslalith.focuslauncher.core.testing.extensions

import app.cash.turbine.testIn
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

context (CoroutineScope)
suspend fun <T, R> Flow<T>.assertFor(
    expected: R,
    debug: Boolean = false,
    valueFor: (T) -> R
) {
    val turbine = testIn(scope = this@CoroutineScope)
    var changedItem = valueFor(turbine.expectMostRecentItem())
    if (debug) println("initial item: $changedItem")

    if (changedItem != expected) {
        changedItem = awaitItemChange(valueFor)
        if (debug) println("changed item: $changedItem")
    }

    turbine.cancelAndIgnoreRemainingEvents()
    if (debug) println("comparing: $changedItem to $expected")
    assertThat(changedItem).isEqualTo(expected)
}
