package dev.mslalith.focuslauncher.core.common.model

import androidx.compose.runtime.Immutable

@Immutable
sealed class State<out T> {
    data object Initial : State<Nothing>()
    data class Success<out R>(val value: R) : State<R>()
}

fun <T> State<T>.getOrNull(): T? = when (this) {
    is State.Success -> this.value
    else -> null
}
