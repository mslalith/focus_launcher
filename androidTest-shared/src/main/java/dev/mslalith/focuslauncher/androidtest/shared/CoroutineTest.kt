package dev.mslalith.focuslauncher.androidtest.shared

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
open class CoroutineTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = MainCoroutineRule()

    protected val testDispatcher = coroutineTestRule.newCoroutineScope()

    protected fun runCoroutineTest(
        testBody: suspend TestScope.() -> Unit
    ) = testDispatcher.runTest(testBody = testBody)
}
