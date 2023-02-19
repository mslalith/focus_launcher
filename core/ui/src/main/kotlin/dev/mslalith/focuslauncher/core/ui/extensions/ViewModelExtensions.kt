package dev.mslalith.focuslauncher.core.ui.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

context(ViewModel)
fun <T> Flow<T>.withinScope(
    initialValue: T
) = stateIn(
    scope = viewModelScope,
    started = SharingStarted.Lazily,
    initialValue = initialValue
)
