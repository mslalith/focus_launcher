package dev.mslalith.focuslauncher.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.testIn
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.androidtest.shared.CoroutineTest
import dev.mslalith.focuslauncher.androidtest.shared.TestApps
import dev.mslalith.focuslauncher.androidtest.shared.awaitItemAndCancel
import dev.mslalith.focuslauncher.data.database.AppDatabase
import dev.mslalith.focuslauncher.data.dto.AppToRoomMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class AppDrawerRepoTest : CoroutineTest() {

    private lateinit var database: AppDatabase
    private lateinit var appDrawerRepo: AppDrawerRepo

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries().build()
        appDrawerRepo = AppDrawerRepo(
            appsDao = database.appsDao(),
            appToRoomMapper = AppToRoomMapper()
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getAllAppsFlow() = runCoroutineTest {
        val testApps = listOf(TestApps.Chrome, TestApps.Youtube)

        var apps = appDrawerRepo.allAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEmpty()

        appDrawerRepo.addApps(testApps)
        apps = appDrawerRepo.allAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEqualTo(testApps)
    }

    @Test
    fun getAppBy() = runCoroutineTest {
        val app = TestApps.Chrome
        appDrawerRepo.addApp(app)
        val dbApp = appDrawerRepo.getAppBy(app.packageName)
        assertThat(app).isEqualTo(dbApp)
    }

    @Test
    fun addApps() = runCoroutineTest {
        val testApps = listOf(TestApps.Chrome, TestApps.Youtube)

        var apps = appDrawerRepo.allAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEmpty()

        appDrawerRepo.addApps(testApps)
        apps = appDrawerRepo.allAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEqualTo(testApps)
    }

    @Test
    fun addApp() = runCoroutineTest {
        val testApp = TestApps.Chrome

        var apps = appDrawerRepo.allAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEmpty()

        appDrawerRepo.addApp(testApp)
        apps = appDrawerRepo.allAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEqualTo(listOf(testApp))
    }

    @Test
    fun removeApp() = runCoroutineTest {
        val testApps = listOf(TestApps.Chrome, TestApps.Youtube)
        val appToRemove = testApps.first()
        val appsAfterRemoving = testApps.filter { it.packageName != appToRemove.packageName }

        var apps = appDrawerRepo.allAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEmpty()

        appDrawerRepo.addApps(testApps)
        apps = appDrawerRepo.allAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEqualTo(testApps)

        appDrawerRepo.removeApp(appToRemove)
        apps = appDrawerRepo.allAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEqualTo(appsAfterRemoving)
    }

    @Test
    fun areAppsEmptyInDatabase() = runCoroutineTest {
        val apps = listOf(TestApps.Chrome, TestApps.Youtube)
        appDrawerRepo.addApps(apps)
        assertThat(appDrawerRepo.areAppsEmptyInDatabase()).isFalse()
        apps.forEach { appDrawerRepo.removeApp(it) }
        assertThat(appDrawerRepo.areAppsEmptyInDatabase()).isTrue()
    }
}
