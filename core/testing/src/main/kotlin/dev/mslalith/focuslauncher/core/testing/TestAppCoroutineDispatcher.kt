package dev.mslalith.focuslauncher.core.testing

import dev.mslalith.focuslauncher.core.common.AppCoroutineDispatcher
import kotlin.coroutines.CoroutineContext

class TestAppCoroutineDispatcher(coroutineContext: CoroutineContext) : AppCoroutineDispatcher {
    override val main: CoroutineContext = coroutineContext
    override val io: CoroutineContext = coroutineContext
}
