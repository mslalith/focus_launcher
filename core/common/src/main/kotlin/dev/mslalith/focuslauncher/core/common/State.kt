package dev.mslalith.focuslauncher.core.common

import dev.mslalith.focuslauncher.core.model.UiText

sealed class State<out T> {
    object Initial : State<Nothing>()
    data class Success<out R>(val value: R) : State<R>()
    data class Error(val uiText: UiText) : State<Nothing>()
}

fun <T> State<T>.getOrNull(): T? = when (this) {
    is State.Success -> this.value
    else -> null
}
