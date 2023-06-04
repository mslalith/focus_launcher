package dev.mslalith.focuslauncher.core.domain.iconpack

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.model.IconPackLoadEvent
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.launcherapps.TestIconPackManager
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class ReloadIconPackAfterFirstLoadUseCaseTest : CoroutineTest() {

    private lateinit var useCase: ReloadIconPackAfterFirstLoadUseCase
    private lateinit var loadIconPackUseCase: LoadIconPackUseCase

    private val testIconPackManager = TestIconPackManager()

    @Before
    fun setup() {
        useCase = ReloadIconPackAfterFirstLoadUseCase(
            iconPackManager = testIconPackManager,
            reloadIconPackUseCase = ReloadIconPackUseCase(iconPackManager = testIconPackManager)
        )
        loadIconPackUseCase = LoadIconPackUseCase(iconPackManager = testIconPackManager)
    }

    @Test
    fun `01 - wait until icon pack is loaded and run the use case`() = runCoroutineTest {
        val iconPackType = IconPackType.System

        backgroundScope.launch {
            testIconPackManager.iconPackLoadEventFlow.test {
                assertThat(awaitItem()).isEqualTo(IconPackLoadEvent.Loading(iconPackType = iconPackType))
                assertThat(awaitItem()).isEqualTo(IconPackLoadEvent.Loaded(iconPackType = iconPackType))
                assertThat(awaitItem()).isEqualTo(IconPackLoadEvent.Reloading(iconPackType = iconPackType))
                assertThat(awaitItem()).isEqualTo(IconPackLoadEvent.Reloaded(iconPackType = iconPackType))
            }
        }

        loadIconPackUseCase(iconPackType = iconPackType)
        useCase()
    }
}
