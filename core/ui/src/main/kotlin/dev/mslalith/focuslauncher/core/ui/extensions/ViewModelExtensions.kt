package dev.mslalith.focuslauncher.core.ui.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

context (ViewModel)
fun <T> Flow<T>.withinScope(
    initialValue: T
) = stateIn(
    scope = viewModelScope,
    started = SharingStarted.Lazily,
    initialValue = initialValue
)

context (ViewModel)
fun launchIn(
    context: CoroutineContext,
    block: suspend CoroutineScope.() -> Unit
) = viewModelScope.launch(context = context, block = block)

context (ViewModel)
fun AppCoroutineDispatcher.launchInIO(
    block: suspend CoroutineScope.() -> Unit
) = launchIn(context = io, block = block)

context (ViewModel)
fun AppCoroutineDispatcher.launchInMain(
    block: suspend CoroutineScope.() -> Unit
) = launchIn(context = main, block = block)
