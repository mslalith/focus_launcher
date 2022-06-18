package dev.mslalith.focuslauncher.data.utils

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

interface AppCoroutineDispatcher {
    val main: CoroutineContext
    val io: CoroutineContext
}

internal class AppCoroutineDispatcherImpl : AppCoroutineDispatcher {
    override val main: CoroutineContext = Dispatchers.Main
    override val io: CoroutineContext = Dispatchers.IO
}
