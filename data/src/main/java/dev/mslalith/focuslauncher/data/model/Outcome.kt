package dev.mslalith.focuslauncher.data.model

sealed class Outcome<out T> {
    data class Success<out R>(val value: R) : Outcome<R>()
    data class Error(val message: String) : Outcome<Nothing>()
    object None : Outcome<Nothing>()
}
