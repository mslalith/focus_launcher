package dev.mslalith.focuslauncher.core.testing.extensions

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

context (CoroutineScope)
suspend fun <T, R> Flow<T>.assertFor(
    expected: R,
    valueFor: (T) -> R
) {
    val changedItem = awaitItemChange(valueFor)
    assertThat(changedItem).isEqualTo(expected)
}
