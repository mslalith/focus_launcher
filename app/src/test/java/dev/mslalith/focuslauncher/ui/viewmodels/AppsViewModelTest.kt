package dev.mslalith.focuslauncher.ui.viewmodels

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import app.cash.turbine.testIn
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.androidtest.shared.CoroutineTest
import dev.mslalith.focuslauncher.androidtest.shared.TestAppCoroutineDispatcher
import dev.mslalith.focuslauncher.androidtest.shared.TestApps
import dev.mslalith.focuslauncher.androidtest.shared.awaitItemAndCancel
import dev.mslalith.focuslauncher.data.database.AppDatabase
import dev.mslalith.focuslauncher.data.dto.AppToRoomMapper
import dev.mslalith.focuslauncher.data.dto.FavoriteToRoomMapper
import dev.mslalith.focuslauncher.data.dto.HiddenToRoomMapper
import dev.mslalith.focuslauncher.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.data.repository.HiddenAppsRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class AppsViewModelTest : CoroutineTest() {

    private lateinit var appsViewModel: AppsViewModel
    private lateinit var appDrawerRepo: AppDrawerRepo
    private lateinit var hiddenAppsRepo: HiddenAppsRepo
    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appToRoomMapper = AppToRoomMapper()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
        appDrawerRepo = AppDrawerRepo(
            appsDao = database.appsDao(),
            appToRoomMapper = appToRoomMapper
        )
        hiddenAppsRepo = HiddenAppsRepo(
            appsDao = database.appsDao(),
            hiddenAppsDao = database.hiddenAppsDao(),
            appToRoomMapper = appToRoomMapper,
            hiddenToRoomMapper = HiddenToRoomMapper(
                appsDao = database.appsDao(),
                appToRoomMapper = appToRoomMapper
            )
        )
        appsViewModel = AppsViewModel(
            appDrawerRepo = appDrawerRepo,
            favoritesRepo = FavoritesRepo(
                appsDao = database.appsDao(),
                favoriteAppsDao = database.favoriteAppsDao(),
                appToRoomMapper = appToRoomMapper,
                favoriteToRoomMapper = FavoriteToRoomMapper(
                    appsDao = database.appsDao(),
                    appToRoomMapper = appToRoomMapper
                )
            ),
            hiddenAppsRepo = hiddenAppsRepo,
            appCoroutineDispatcher = TestAppCoroutineDispatcher(coroutineContext = testDispatcher.coroutineContext)
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    private suspend fun addTestApps() {
        appDrawerRepo.addApps(TestApps.all)
    }

    @Test
    fun `add to favorite behaviour`() = runCoroutineTest {
        addTestApps()
        val testApp = TestApps.Chrome

        val apps = appsViewModel.appDrawerAppsStateFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).contains(testApp)

        appsViewModel.onlyFavoritesStateFlow.test {
            assertThat(awaitItem()).isEmpty()
            appsViewModel.addToFavorites(testApp)
            assertThat(awaitItem()).isEqualTo(listOf(testApp))
        }
    }

    @Test
    fun `hide app behaviour`() = runCoroutineTest {
        addTestApps()
        val testApp = TestApps.Chrome

        val apps = appsViewModel.appDrawerAppsStateFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).contains(testApp)

        hiddenAppsRepo.onlyHiddenAppsFlow.test {
            assertThat(awaitItem()).isEmpty()
            appsViewModel.addToHiddenApps(testApp)
            assertThat(awaitItem()).isEqualTo(listOf(testApp))
        }
    }

    @Test
    fun `install app behaviour`() = runCoroutineTest {
        val testApp = TestApps.Chrome
        appsViewModel.appDrawerAppsStateFlow.test {
            assertThat(awaitItem()).isEmpty()
            appsViewModel.handleAppInstall(testApp)
            assertThat(awaitItem()).contains(testApp)
        }
    }

    @Test
    fun `uninstall app behaviour`() = runCoroutineTest {
        addTestApps()
        val testApp = TestApps.Chrome

        appsViewModel.appDrawerAppsStateFlow.test {
            assertThat(awaitItem()).contains(testApp)
            appsViewModel.handleAppUninstall(testApp.packageName)
            assertThat(awaitItem()).doesNotContain(testApp)
        }
    }
}
