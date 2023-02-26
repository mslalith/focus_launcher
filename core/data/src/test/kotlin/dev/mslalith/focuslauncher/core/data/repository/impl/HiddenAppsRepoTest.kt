package dev.mslalith.focuslauncher.core.data.repository.impl

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.core.data.dto.AppToRoomMapper
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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
internal class HiddenAppsRepoTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: HiddenAppsRepoImpl

    @Inject
    lateinit var appsDao: AppsDao

    @BindValue
    val appToRoomMapper: AppToRoomMapper = AppToRoomMapper()

    @Before
    fun setup() {
        hiltRule.inject()
        runBlocking {
            appsDao.addApps(TestApps.all.map(appToRoomMapper::toEntity))
        }
    }

    @Test
    fun `initially hidden apps must be empty`() = runCoroutineTest {
        val items = repo.onlyHiddenAppsFlow.awaitItem()
        assertThat(items).isEmpty()
    }

    @Test
    fun `when an app is hidden, make sure it stays hidden`() = runCoroutineTest {
        val app = TestApps.Chrome
        repo.addToHiddenApps(app)

        val items = repo.onlyHiddenAppsFlow.awaitItem()
        assertThat(items).isEqualTo(listOf(app))
    }

    @Test
    fun `when multiple apps are hidden, make sure it stays hidden`() = runCoroutineTest {
        val apps = listOf(TestApps.Chrome, TestApps.Phone)
        repo.addToHiddenApps(apps)

        val items = repo.onlyHiddenAppsFlow.awaitItem()
        assertThat(items).isEqualTo(apps)
    }

    @Test
    fun `when an app is un-hidden, make sure it isn't present`() = runCoroutineTest {
        val app = TestApps.Chrome
        repo.addToHiddenApps(app)

        var items = repo.onlyHiddenAppsFlow.awaitItem()
        assertThat(items).isEqualTo(listOf(app))

        repo.removeFromHiddenApps(app.packageName)

        items = repo.onlyHiddenAppsFlow.awaitItem()
        assertThat(items).doesNotContain(app)
        assertThat(items).isEmpty()
    }

    @Test
    fun `when hidden apps are cleared, list should be empty`() = runCoroutineTest {
        val apps = TestApps.all
        repo.addToHiddenApps(apps)

        var items = repo.onlyHiddenAppsFlow.awaitItem()
        assertThat(items).isEqualTo(apps)

        repo.clearHiddenApps()

        items = repo.onlyHiddenAppsFlow.awaitItem()
        assertThat(items).isEmpty()
    }

    @Test
    fun `when querying for a hidden app, isHidden must return true`() = runCoroutineTest {
        val app = TestApps.Youtube
        repo.addToHiddenApps(app)

        val items = repo.onlyHiddenAppsFlow.awaitItem()
        assertThat(items).isEqualTo(listOf(app))

        val isHidden = repo.isHidden(app.packageName)
        assertThat(isHidden).isTrue()
    }

    @Test
    fun `when querying for an un-hidden app, isHidden must return false`() = runCoroutineTest {
        val app = TestApps.Chrome
        repo.addToHiddenApps(app)

        val items = repo.onlyHiddenAppsFlow.awaitItem()
        assertThat(items).isEqualTo(listOf(app))

        val isHidden = repo.isHidden(TestApps.Phone.packageName)
        assertThat(isHidden).isFalse()
    }
}
