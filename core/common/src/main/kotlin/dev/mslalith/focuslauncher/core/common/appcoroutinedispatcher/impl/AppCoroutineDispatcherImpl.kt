package dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.impl

import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

internal class AppCoroutineDispatcherImpl : AppCoroutineDispatcher {
    override val main: CoroutineContext = Dispatchers.Main
    override val io: CoroutineContext = Dispatchers.IO
}
