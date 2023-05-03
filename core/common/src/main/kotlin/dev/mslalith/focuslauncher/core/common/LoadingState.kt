package dev.mslalith.focuslauncher.core.common

sealed interface LoadingState<out T> {
    object Loading : LoadingState<Nothing>
    data class Loaded<out R>(val value: R) : LoadingState<R>
}

fun <T> LoadingState<T>.getOrNull(): T? = when (this) {
    is LoadingState.Loaded -> this.value
    else -> null
}
