package dev.mslalith.focuslauncher.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.androidtest.shared.TestApps
import dev.mslalith.focuslauncher.data.database.AppDatabase
import dev.mslalith.focuslauncher.data.dto.AppToRoomMapper
import dev.mslalith.focuslauncher.data.dto.HiddenToRoomMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HiddenAppsRepoTest {

    private lateinit var database: AppDatabase
    private lateinit var hiddenAppsRepo: HiddenAppsRepo

    @Before
    fun setUp() = runTest {
        val appToRoomMapper = AppToRoomMapper()
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        database.appsDao().addApps(TestApps.all.map(appToRoomMapper::toEntity))
        hiddenAppsRepo = HiddenAppsRepo(
            appsDao = database.appsDao(),
            hiddenAppsDao = database.hiddenAppsDao(),
            appToRoomMapper = appToRoomMapper,
            hiddenToRoomMapper = HiddenToRoomMapper(
                appsDao = database.appsDao(),
                appToRoomMapper = appToRoomMapper
            )
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getOnlyHiddenAppsFlow() = runTest {
        val apps = listOf(TestApps.Chrome, TestApps.Phone)
        val job = launch {
            hiddenAppsRepo.onlyHiddenAppsFlow.test {
                var appsList = awaitItem()
                assertThat(appsList).isEmpty()
                appsList = awaitItem()
                assertThat(appsList.size).isEqualTo(apps.size)
                assertThat(appsList).isEqualTo(apps)
                expectNoEvents()
            }
        }

        apps.forEach { hiddenAppsRepo.addToHiddenApps(it) }
        job.join()
    }

    @Test
    fun addToHiddenApps() = runTest {
        val app = TestApps.Chrome
        val job = launch {
            hiddenAppsRepo.onlyHiddenAppsFlow.test {
                val appsList = awaitItem()
                assertThat(appsList.size).isEqualTo(1)
                assertThat(appsList).isEqualTo(listOf(app))
                expectNoEvents()
            }
        }

        hiddenAppsRepo.addToHiddenApps(app)
        job.join()
    }

    @Test
    fun removeFromHiddenApps() = runTest {
        val apps = listOf(TestApps.Chrome, TestApps.Phone)
        val appToRemove = apps.first()
        val appsAfterRemoving = apps.filter { it.packageName != appToRemove.packageName }

        val job = launch {
            hiddenAppsRepo.onlyHiddenAppsFlow.test {
                var appsList = awaitItem()
                assertThat(appsList.size).isEqualTo(apps.size)
                assertThat(appsList).isEqualTo(apps)
                appsList = awaitItem()
                assertThat(appsList.size).isEqualTo(appsAfterRemoving.size)
                assertThat(appsList).isEqualTo(appsAfterRemoving)
                expectNoEvents()
            }
        }

        apps.forEach { hiddenAppsRepo.addToHiddenApps(it) }
        hiddenAppsRepo.removeFromHiddenApps(appToRemove.packageName)
        job.join()
    }

    @Test
    fun clearHiddenApps() = runTest {
        val apps = listOf(TestApps.Chrome, TestApps.Phone)
        val job = launch {
            hiddenAppsRepo.onlyHiddenAppsFlow.test {
                var appsList = awaitItem()
                assertThat(appsList.size).isEqualTo(apps.size)
                assertThat(appsList).isEqualTo(apps)
                appsList = awaitItem()
                assertThat(appsList).isEmpty()
                expectNoEvents()
            }
        }

        apps.forEach { hiddenAppsRepo.addToHiddenApps(it) }
        hiddenAppsRepo.clearHiddenApps()
        job.join()
    }
}
