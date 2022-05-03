package dev.mslalith.focuslauncher.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.androidtest.shared.TestApps
import dev.mslalith.focuslauncher.data.database.AppDatabase
import dev.mslalith.focuslauncher.data.dto.AppToRoomMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppDrawerRepoTest {

    private lateinit var database: AppDatabase
    private lateinit var appDrawerRepo: AppDrawerRepo

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
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
    fun getAllAppsFlow() = runTest {
        val apps = listOf(TestApps.Chrome, TestApps.Youtube)
        val job = launch {
            appDrawerRepo.allAppsFlow.test {
                val appsList = awaitItem()
                assertThat(appsList.size).isEqualTo(apps.size)
                assertThat(appsList).isEqualTo(apps)
                expectNoEvents()
            }
        }

        appDrawerRepo.addApps(apps)
        job.join()
    }

    @Test
    fun getAppBy() = runTest {
        val app = TestApps.Chrome
        appDrawerRepo.addApp(app)
        val dbApp = appDrawerRepo.getAppBy(app.packageName)
        assertThat(app).isEqualTo(dbApp)
    }

    @Test
    fun addApps() = runTest {
        val apps = listOf(TestApps.Chrome, TestApps.Youtube)
        val job = launch {
            appDrawerRepo.allAppsFlow.test {
                val appsList = awaitItem()
                assertThat(appsList.size).isEqualTo(apps.size)
                assertThat(appsList).isEqualTo(apps)
                expectNoEvents()
            }
        }

        appDrawerRepo.addApps(apps)
        job.join()
    }

    @Test
    fun addApp() = runTest {
        val app = TestApps.Chrome
        val job = launch {
            appDrawerRepo.allAppsFlow.test {
                val appsList = awaitItem()
                assertThat(appsList.size).isEqualTo(1)
                assertThat(appsList).isEqualTo(listOf(app))
                expectNoEvents()
            }
        }

        appDrawerRepo.addApp(app)
        job.join()
    }

    @Test
    fun removeApp() = runTest {
        val apps = listOf(TestApps.Chrome, TestApps.Youtube)
        val appToRemove = apps.first()
        val appsAfterRemoving = apps.filter { it.packageName != appToRemove.packageName }

        val job = launch {
            appDrawerRepo.allAppsFlow.test {
                var appsList = awaitItem()
                assertThat(appsList.size).isEqualTo(apps.size)
                assertThat(appsList).isEqualTo(apps)
                appsList = awaitItem()
                assertThat(appsList.size).isEqualTo(appsAfterRemoving.size)
                assertThat(appsList).isEqualTo(appsAfterRemoving)
                expectNoEvents()
            }
        }

        appDrawerRepo.addApps(apps)
        appDrawerRepo.removeApp(appToRemove)
        job.join()
    }

    @Test
    fun areAppsEmptyInDatabase() = runTest {
        val apps = listOf(TestApps.Chrome, TestApps.Youtube)
        appDrawerRepo.addApps(apps)
        assertThat(appDrawerRepo.areAppsEmptyInDatabase()).isFalse()
        apps.forEach { appDrawerRepo.removeApp(it) }
        assertThat(appDrawerRepo.areAppsEmptyInDatabase()).isTrue()
    }
}
