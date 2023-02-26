package dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher

import kotlinx.coroutines.CoroutineDispatcher

interface AppCoroutineDispatcher {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}
