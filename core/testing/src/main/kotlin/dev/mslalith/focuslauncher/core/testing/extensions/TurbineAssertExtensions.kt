package dev.mslalith.focuslauncher.core.testing.extensions

import app.cash.turbine.testIn
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

context (CoroutineScope)
suspend fun <T, R> Flow<T>.assertFor(
    expected: R,
    valueFor: (T) -> R
) {
    val turbine = testIn(scope = this@CoroutineScope)
    var changedItem = valueFor(turbine.expectMostRecentItem())

    if (changedItem != expected) {
        changedItem = awaitItemChange(valueFor)
    }

    turbine.cancelAndIgnoreRemainingEvents()
    assertThat(changedItem).isEqualTo(expected)
}
