package dev.mslalith.focuslauncher.core.domain.iconpack

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.domain.utils.toIconPacks
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.test.TestIconPackManager
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
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
class FetchIconPacksUseCaseTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var testIconPackManager: TestIconPackManager

    private lateinit var useCase: FetchIconPacksUseCase

    @Before
    fun setup() {
        hiltRule.inject()
        useCase = FetchIconPacksUseCase(iconPackManager = testIconPackManager)
    }

    @Test
    fun `01 - fetch icon packs must return proper list`() = runCoroutineTest {
        val iconPacks = TestApps.all.toIconPacks()

        testIconPackManager.setIconPackApps(iconPacks = iconPacks)
        useCase()

        assertThat(testIconPackManager.iconPacksFlow.awaitItem()).isEqualTo(iconPacks)
    }
}
