package dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.test

import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class TestAppCoroutineDispatcher(
    coroutineContext: CoroutineContext = Dispatchers.Main // this is swapped with TestDispatcher in tests
) : AppCoroutineDispatcher {
    override val main: CoroutineContext = coroutineContext
    override val io: CoroutineContext = coroutineContext
}
