package dev.mslalith.focuslauncher.core.data.repository

import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
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
internal class AppDrawerRepoTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: AppDrawerRepo

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun `initially app drawer list must be empty`() = runCoroutineTest {
        val items = repo.allAppsFlow.awaitItem()
        Truth.assertThat(items).isEmpty()
    }

    @Test
    fun `when an app is added to app drawer, make sure it stays added`() = runCoroutineTest {
        val app = TestApps.Chrome
        repo.addApp(app)

        val items = repo.allAppsFlow.awaitItem()
        Truth.assertThat(items).isEqualTo(listOf(app))
    }

    @Test
    fun `when multiple apps are added to app drawer, make sure they stays added`() = runCoroutineTest {
        val apps = listOf(TestApps.Chrome, TestApps.Phone)
        repo.addApps(apps)

        val items = repo.allAppsFlow.awaitItem()
        Truth.assertThat(items).isEqualTo(apps)
    }

    @Test
    fun `when an app is removed from app drawer, make sure it isn't present`() = runCoroutineTest {
        val app = TestApps.Chrome
        repo.addApp(app)

        var items = repo.allAppsFlow.awaitItem()
        Truth.assertThat(items).isEqualTo(listOf(app))

        repo.removeApp(app)

        items = repo.allAppsFlow.awaitItem()
        Truth.assertThat(items).doesNotContain(app)
        Truth.assertThat(items).isEmpty()
    }

    @Test
    fun `when apps are present in app drawer, list should not be empty`() = runCoroutineTest {
        repo.addApps(TestApps.all)
        Truth.assertThat(repo.areAppsEmptyInDatabase()).isFalse()
    }

    @Test
    fun `when apps are not present in app drawer, list should be empty`() = runCoroutineTest {
        Truth.assertThat(repo.allAppsFlow.awaitItem()).isEmpty()
        Truth.assertThat(repo.areAppsEmptyInDatabase()).isTrue()
    }

    @Test
    fun `when fetching for an existing app drawer app, getAppBy must return that app`() = runCoroutineTest {
        val app = TestApps.Youtube
        repo.addApp(app)

        val items = repo.allAppsFlow.awaitItem()
        Truth.assertThat(items).isEqualTo(listOf(app))

        val fetchedApp = repo.getAppBy(app.packageName)
        Truth.assertThat(fetchedApp).isEqualTo(app)
    }

    @Test
    fun `when fetching for a non-existing app drawer app, getAppBy must return null`() = runCoroutineTest {
        val app = TestApps.Youtube
        repo.addApp(app)

        val items = repo.allAppsFlow.awaitItem()
        Truth.assertThat(items).isEqualTo(listOf(app))

        val fetchedApp = repo.getAppBy("some.package")
        Truth.assertThat(fetchedApp).isNull()
    }

    @Test
    fun `when display name for an app drawer app is changed, new name must be returned`() = runCoroutineTest {
        val app = TestApps.Youtube
        repo.addApp(app)

        val items = repo.allAppsFlow.awaitItem()
        Truth.assertThat(items).isEqualTo(listOf(app))

        val displayName = "You Tube"
        repo.updateDisplayName(app, displayName)

        val fetchedApp = repo.getAppBy(app.packageName)
        Truth.assertThat(fetchedApp).isNotNull()
        Truth.assertThat(fetchedApp?.displayName).isEqualTo(displayName)
    }

    @Test
    fun `when display name for an app drawer app is not set, app name must be used`() = runCoroutineTest {
        val app = TestApps.Youtube
        repo.addApp(app)

        val items = repo.allAppsFlow.awaitItem()
        Truth.assertThat(items.map { it.displayName }).isEqualTo(listOf(app.displayName))
    }
}
