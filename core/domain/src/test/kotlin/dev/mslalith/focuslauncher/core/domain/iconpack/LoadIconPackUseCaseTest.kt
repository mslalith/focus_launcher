package dev.mslalith.focuslauncher.core.domain.iconpack

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.test.TestIconPackManager
import dev.mslalith.focuslauncher.core.model.IconPackLoadEvent
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class LoadIconPackUseCaseTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var testIconPackManager: TestIconPackManager

    private lateinit var useCase: LoadIconPackUseCase

    @Before
    fun setup() {
        hiltRule.inject()
        useCase = LoadIconPackUseCase(iconPackManager = testIconPackManager)
    }

    @Test
    fun `01 - load icon pack`() = runCoroutineTest {
        backgroundScope.launch {
            testIconPackManager.iconPackLoadEventFlow.test {
                assertThat(awaitItem()).isEqualTo(IconPackLoadEvent.Loading)
                assertThat(awaitItem()).isEqualTo(IconPackLoadEvent.Loaded)
            }
        }
        useCase(iconPackType = IconPackType.System)
    }
}
