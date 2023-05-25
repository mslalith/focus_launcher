package dev.mslalith.focuslauncher.core.domain.iconpack

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
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
class GetIconPackAppsUseCaseTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    private val testIconPackManager = TestIconPackManager()

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    private lateinit var useCase: GetIconPackAppsUseCase

    @Before
    fun setup() {
        hiltRule.inject()
        useCase = GetIconPackAppsUseCase(
            iconPackManager = testIconPackManager,
            appDrawerRepo = appDrawerRepo
        )
    }

    @Test
    fun `01 - get icon packs must return proper list`() = runCoroutineTest {
        val allApps = TestApps.all
        val expectedApps = allApps.take(n = 1)
        val iconPacks = expectedApps.toIconPacks()

        appDrawerRepo.addApps(apps = allApps)
        testIconPackManager.setIconPackApps(iconPacks = iconPacks)

        assertThat(useCase().awaitItem()).isEqualTo(expectedApps)
    }
}
