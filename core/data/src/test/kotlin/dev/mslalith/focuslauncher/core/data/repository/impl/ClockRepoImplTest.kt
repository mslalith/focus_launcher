package dev.mslalith.focuslauncher.core.data.repository.impl

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.data.base.RepoTest
import dev.mslalith.focuslauncher.core.data.model.TestComponents
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
internal class ClockRepoImplTest : RepoTest<ClockRepoImpl>() {

    override fun provideRepo(testComponents: TestComponents) = ClockRepoImpl()

    @Test
    fun `when refreshed, the clock time should be updated`() = runCoroutineTest {
        val oldTime = repo.currentInstantStateFlow.awaitItem()
        repo.refreshTime()

        val newTime = repo.currentInstantStateFlow.awaitItem()
        assertThat(newTime).isGreaterThan(oldTime)
    }
}
