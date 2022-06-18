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
import dev.mslalith.focuslauncher.data.dto.HiddenToRoomMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private val appToRoomMapper = AppToRoomMapper()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries().build()
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

    private suspend fun addTestApps() {
        database.appsDao().addApps(TestApps.all.map(appToRoomMapper::toEntity))
    }

    @Test
    fun getOnlyHiddenAppsFlow() = runCoroutineTest {
        addTestApps()
        val testApps = listOf(TestApps.Chrome, TestApps.Phone)

        var apps = hiddenAppsRepo.onlyHiddenAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEmpty()

        hiddenAppsRepo.addToHiddenApps(testApps)
        apps = hiddenAppsRepo.onlyHiddenAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEqualTo(testApps)
    }

    @Test
    fun addToHiddenApps() = runCoroutineTest {
        addTestApps()
        val testApp = TestApps.Chrome
        var apps = hiddenAppsRepo.onlyHiddenAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEmpty()

        hiddenAppsRepo.addToHiddenApps(testApp)
        apps = hiddenAppsRepo.onlyHiddenAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEqualTo(listOf(testApp))
    }

    @Test
    fun removeFromHiddenApps() = runCoroutineTest {
        addTestApps()
        val testApps = listOf(TestApps.Chrome, TestApps.Phone)
        val appToRemove = testApps.first()
        val appsAfterRemoving = testApps.filter { it.packageName != appToRemove.packageName }

        var apps = hiddenAppsRepo.onlyHiddenAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEmpty()

        hiddenAppsRepo.addToHiddenApps(testApps)
        apps = hiddenAppsRepo.onlyHiddenAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEqualTo(testApps)

        hiddenAppsRepo.removeFromHiddenApps(appToRemove.packageName)
        apps = hiddenAppsRepo.onlyHiddenAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEqualTo(appsAfterRemoving)
    }

    @Test
    fun clearHiddenApps() = runCoroutineTest {
        addTestApps()
        val testApps = listOf(TestApps.Chrome, TestApps.Phone)

        var apps = hiddenAppsRepo.onlyHiddenAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEmpty()

        hiddenAppsRepo.addToHiddenApps(testApps)
        apps = hiddenAppsRepo.onlyHiddenAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEqualTo(testApps)

        hiddenAppsRepo.clearHiddenApps()
        apps = hiddenAppsRepo.onlyHiddenAppsFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEmpty()
    }
}
