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
import dev.mslalith.focuslauncher.data.dto.FavoriteToRoomMapper
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
class FavoritesRepoTest : CoroutineTest() {

    private lateinit var database: AppDatabase
    private lateinit var favoritesRepo: FavoritesRepo

    @Before
    fun setUp() = runCoroutineTest {
        val appToRoomMapper = AppToRoomMapper()
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries().build()
        database.appsDao().addApps(TestApps.all.map(appToRoomMapper::toEntity))
        favoritesRepo = FavoritesRepo(
            appsDao = database.appsDao(),
            favoriteAppsDao = database.favoriteAppsDao(),
            appToRoomMapper = appToRoomMapper,
            favoriteToRoomMapper = FavoriteToRoomMapper(
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
    fun getOnlyFavoritesFlow() = runCoroutineTest {
        val apps = listOf(TestApps.Chrome, TestApps.Phone)
        val job = launch {
            favoritesRepo.onlyFavoritesFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(apps)
                expectNoEvents()
            }
        }

        advanceTimeBy(100)
        favoritesRepo.addToFavorites(apps)
        job.join()
    }

    @Test
    fun addToFavorites() = runCoroutineTest {
        val app = TestApps.Chrome
        val job = launch {
            favoritesRepo.onlyFavoritesFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(listOf(app))
                expectNoEvents()
            }
        }

        advanceTimeBy(100)
        favoritesRepo.addToFavorites(app)
        job.join()
    }

    @Test
    fun reorderFavorite() = runCoroutineTest {
        val initialFavorites = listOf(TestApps.Chrome, TestApps.Phone, TestApps.Youtube)
        favoritesRepo.addToFavorites(initialFavorites)
        val reorderedFavorites = initialFavorites.asReversed()

        val job = launch {
            favoritesRepo.onlyFavoritesFlow.test {
                assertThat(awaitItem()).isEqualTo(initialFavorites)
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(reorderedFavorites)
                expectNoEvents()
            }
        }

        advanceTimeBy(100)
        favoritesRepo.reorderFavorite(initialFavorites.first(), initialFavorites.last())
        job.join()
    }

    @Test
    fun removeFromFavorites() = runCoroutineTest {
        val apps = listOf(TestApps.Chrome, TestApps.Phone)
        val appToRemove = apps.first()
        val appsAfterRemoving = apps.filter { it.packageName != appToRemove.packageName }

        val job = launch {
            favoritesRepo.onlyFavoritesFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(apps)
                assertThat(awaitItem()).isEqualTo(appsAfterRemoving)
                expectNoEvents()
            }
        }

        advanceTimeBy(100)
        favoritesRepo.addToFavorites(apps)
        advanceTimeBy(100)
        favoritesRepo.removeFromFavorites(appToRemove.packageName)
        job.join()
    }

    @Test
    fun clearFavorites() = runCoroutineTest {
        val apps = listOf(TestApps.Chrome, TestApps.Youtube)
        val job = launch {
            favoritesRepo.onlyFavoritesFlow.test {
                assertThat(awaitItem()).isEmpty()
                assertThat(awaitItem()).isEqualTo(apps)
                assertThat(awaitItem()).isEmpty()
                expectNoEvents()
            }
        }

        advanceTimeBy(100)
        favoritesRepo.addToFavorites(apps)
        advanceTimeBy(100)
        favoritesRepo.clearFavorites()
        job.join()
    }

    @Test
    fun isFavorite() = runCoroutineTest {
        val app = TestApps.Chrome
        favoritesRepo.addToFavorites(app)
        assertThat(favoritesRepo.isFavorite(app.packageName)).isTrue()
        favoritesRepo.removeFromFavorites(app.packageName)
        assertThat(favoritesRepo.isFavorite(app.packageName)).isFalse()
    }
}
