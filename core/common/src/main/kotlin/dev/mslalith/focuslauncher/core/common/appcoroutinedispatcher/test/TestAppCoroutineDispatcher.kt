package dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.test

import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher

class TestAppCoroutineDispatcher(coroutineDispatcher: CoroutineDispatcher) : AppCoroutineDispatcher {
    override val main: CoroutineDispatcher = coroutineDispatcher
    override val io: CoroutineDispatcher = coroutineDispatcher
}
