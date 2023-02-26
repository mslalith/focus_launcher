package dev.mslalith.focuslauncher.core.data.repository

import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
internal class ClockRepoTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: ClockRepo

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun `when refreshed, the clock time should be updated`() = runCoroutineTest {
        val oldTime = repo.currentInstantStateFlow.awaitItem()
        repo.refreshTime()

        val newTime = repo.currentInstantStateFlow.awaitItem()
        Truth.assertThat(newTime).isGreaterThan(oldTime)
    }
}
