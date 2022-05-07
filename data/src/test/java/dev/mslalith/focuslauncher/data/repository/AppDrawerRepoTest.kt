package dev.mslalith.focuslauncher.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.androidtest.shared.CoroutineTest
import dev.mslalith.focuslauncher.androidtest.shared.TestApps
import dev.mslalith.focuslauncher.data.database.AppDatabase
import dev.mslalith.focuslauncher.data.dto.AppToRoomMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
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
        val apps = listOf(TestApps.Chrome, TestApps.Youtube)
        val job = launch {
            appDrawerRepo.allAppsFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(apps)
                expectNoEvents()
            }
        }

        advanceTimeBy(100)
        appDrawerRepo.addApps(apps)
        job.join()
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
        val apps = listOf(TestApps.Chrome, TestApps.Youtube)
        val job = launch {
            appDrawerRepo.allAppsFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(apps)
                expectNoEvents()
            }
        }

        advanceTimeBy(100)
        appDrawerRepo.addApps(apps)
        job.join()
    }

    @Test
    fun addApp() = runCoroutineTest {
        val app = TestApps.Chrome
        val job = launch {
            appDrawerRepo.allAppsFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(listOf(app))
                expectNoEvents()
            }
        }

        advanceTimeBy(100)
        appDrawerRepo.addApp(app)
        job.join()
    }

    @Test
    fun removeApp() = runCoroutineTest {
        val apps = listOf(TestApps.Chrome, TestApps.Youtube)
        val appToRemove = apps.first()
        val appsAfterRemoving = apps.filter { it.packageName != appToRemove.packageName }

        val job = launch {
            appDrawerRepo.allAppsFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(apps)
                assertThat(awaitItem()).isEqualTo(appsAfterRemoving)
                expectNoEvents()
            }
        }

        advanceTimeBy(100)
        appDrawerRepo.addApps(apps)
        advanceTimeBy(100)
        appDrawerRepo.removeApp(appToRemove)
        job.join()
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
