package dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher

import kotlin.coroutines.CoroutineContext

interface AppCoroutineDispatcher {
    val main: CoroutineContext
    val io: CoroutineContext
}
