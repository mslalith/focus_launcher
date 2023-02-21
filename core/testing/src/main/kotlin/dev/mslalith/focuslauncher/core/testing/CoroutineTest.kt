package dev.mslalith.focuslauncher.core.testing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.mslalith.focuslauncher.core.testing.rules.TestCoroutineRule
import dev.mslalith.focuslauncher.core.testing.rules.newCoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
open class CoroutineTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    protected val testDispatcher = coroutineTestRule.newCoroutineScope()

    protected fun runCoroutineTest(
        testBody: suspend TestScope.() -> Unit
    ) = testDispatcher.runTest(testBody = testBody)
}
