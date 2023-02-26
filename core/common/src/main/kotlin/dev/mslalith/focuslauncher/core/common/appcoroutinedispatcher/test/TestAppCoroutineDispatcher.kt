package dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.test

import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import kotlin.coroutines.CoroutineContext

class TestAppCoroutineDispatcher(coroutineContext: CoroutineContext) : AppCoroutineDispatcher {
    override val main: CoroutineContext = coroutineContext
    override val io: CoroutineContext = coroutineContext
}
