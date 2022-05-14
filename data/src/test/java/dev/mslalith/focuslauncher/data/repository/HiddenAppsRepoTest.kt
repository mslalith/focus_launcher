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
import dev.mslalith.focuslauncher.data.dto.HiddenToRoomMapper
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
class HiddenAppsRepoTest : CoroutineTest() {

    private lateinit var database: AppDatabase
    private lateinit var hiddenAppsRepo: HiddenAppsRepo

    @Before
    fun setUp() = runCoroutineTest {
        val appToRoomMapper = AppToRoomMapper()
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries().build()
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
    fun getOnlyHiddenAppsFlow() = runCoroutineTest {
        val apps = listOf(TestApps.Chrome, TestApps.Phone)
        val job = launch {
            hiddenAppsRepo.onlyHiddenAppsFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(apps)
                expectNoEvents()
            }
        }

        advanceTimeBy(100)
        hiddenAppsRepo.addToHiddenApps(apps)
        job.join()
    }

    @Test
    fun addToHiddenApps() = runCoroutineTest {
        val app = TestApps.Chrome
        val job = launch {
            hiddenAppsRepo.onlyHiddenAppsFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(listOf(app))
                expectNoEvents()
            }
        }

        advanceTimeBy(100)
        hiddenAppsRepo.addToHiddenApps(app)
        job.join()
    }

    @Test
    fun removeFromHiddenApps() = runCoroutineTest {
        val apps = listOf(TestApps.Chrome, TestApps.Phone)
        val appToRemove = apps.first()
        val appsAfterRemoving = apps.filter { it.packageName != appToRemove.packageName }

        val job = launch {
            hiddenAppsRepo.onlyHiddenAppsFlow.test {
                assertThat(awaitItem()).isEqualTo(apps)
                assertThat(awaitItem()).isEqualTo(appsAfterRemoving)
                expectNoEvents()
            }
        }

        hiddenAppsRepo.addToHiddenApps(apps)
        advanceTimeBy(100)
        hiddenAppsRepo.removeFromHiddenApps(appToRemove.packageName)
        job.join()
    }

    @Test
    fun clearHiddenApps() = runCoroutineTest {
        val apps = listOf(TestApps.Chrome, TestApps.Phone)
        val job = launch {
            hiddenAppsRepo.onlyHiddenAppsFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(apps)
                assertThat(awaitItem()).isEmpty()
                expectNoEvents()
            }
        }

        advanceTimeBy(100)
        hiddenAppsRepo.addToHiddenApps(apps)
        advanceTimeBy(100)
        hiddenAppsRepo.clearHiddenApps()
        job.join()
    }
}
