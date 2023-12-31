package dev.mslalith.focuslauncher.core.common.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface LoadingState<out T> {
    data object Loading : LoadingState<Nothing>
    data class Loaded<out R>(val value: R) : LoadingState<R>
}

fun <T> LoadingState<T>.getOrNull(): T? = when (this) {
    is LoadingState.Loaded -> this.value
    else -> null
}
