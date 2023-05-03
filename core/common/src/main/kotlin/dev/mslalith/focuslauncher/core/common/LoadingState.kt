package dev.mslalith.focuslauncher.core.common

import javax.annotation.concurrent.Immutable

@Immutable
sealed interface LoadingState<out T> {
    @Immutable
    object Loading : LoadingState<Nothing>

    @Immutable
    data class Loaded<out R>(val value: R) : LoadingState<R>
}

fun <T> LoadingState<T>.getOrNull(): T? = when (this) {
    is LoadingState.Loaded -> this.value
    else -> null
}
