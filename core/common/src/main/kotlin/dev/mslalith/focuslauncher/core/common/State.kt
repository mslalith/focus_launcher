package dev.mslalith.focuslauncher.core.common

sealed class State<out T> {
    object Initial : State<Nothing>()
    data class Success<out R>(val value: R) : State<R>()
    data class Error(val message: String) : State<Nothing>()
}

fun <T> State<T>.getOrNull(): T? = when (this) {
    is State.Success -> this.value
    else -> null
}
