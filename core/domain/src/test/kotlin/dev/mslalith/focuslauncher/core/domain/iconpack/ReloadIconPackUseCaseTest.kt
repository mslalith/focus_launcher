package dev.mslalith.focuslauncher.core.domain.iconpack

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.model.IconPackLoadEvent
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.launcherapps.TestIconPackManager
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AppRobolectricTestRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class ReloadIconPackUseCaseTest : CoroutineTest() {

    private lateinit var useCase: ReloadIconPackUseCase

    private val testIconPackManager = TestIconPackManager()

    @Before
    fun setup() {
        useCase = ReloadIconPackUseCase(iconPackManager = testIconPackManager)
    }

    @Test
    fun `01 - reload icon pack`() = runCoroutineTest {
        backgroundScope.launch {
            testIconPackManager.iconPackLoadEventFlow.test {
                assertThat(awaitItem()).isEqualTo(IconPackLoadEvent.Reloading(iconPackType = IconPackType.System))
                assertThat(awaitItem()).isEqualTo(IconPackLoadEvent.Reloaded(iconPackType = IconPackType.System))
            }
        }
        useCase()
    }
}
