package dev.mslalith.focuslauncher.core.testing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.mslalith.focuslauncher.core.testing.rules.TestCoroutineRule
import dev.mslalith.focuslauncher.core.testing.rules.newCoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule

open class CoroutineTest {

    @get:Rule(order = 1)
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 2)
    val coroutineTestRule = TestCoroutineRule()

    protected val testDispatcher = coroutineTestRule.newCoroutineScope()

    protected fun runCoroutineTest(
        testBody: suspend TestScope.() -> Unit
    ) = testDispatcher.runTest(testBody = testBody)
}
