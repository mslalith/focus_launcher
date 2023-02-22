package dev.mslalith.focuslauncher.core.data

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow

suspend fun <T> verifyChange(
    flow: Flow<T>,
    initialItem: T,
    mutate: suspend () -> T
) = flow.test {
    assertThat(awaitItem()).isEqualTo(initialItem)
    val newItem = mutate()
    assertThat(awaitItem()).isEqualTo(newItem)
}
