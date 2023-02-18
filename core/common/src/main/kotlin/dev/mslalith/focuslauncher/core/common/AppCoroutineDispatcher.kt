package dev.mslalith.focuslauncher.core.common

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

interface AppCoroutineDispatcher {
    val main: CoroutineContext
    val io: CoroutineContext
}

internal class AppCoroutineDispatcherImpl : AppCoroutineDispatcher {
    override val main: CoroutineContext = Dispatchers.Main
    override val io: CoroutineContext = Dispatchers.IO
}
