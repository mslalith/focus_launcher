package dev.mslalith.focuslauncher.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.androidtest.shared.TestApps
import dev.mslalith.focuslauncher.data.database.AppDatabase
import dev.mslalith.focuslauncher.data.dto.AppToRoomMapper
import dev.mslalith.focuslauncher.data.dto.FavoriteToRoomMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesRepoTest {

    private lateinit var database: AppDatabase
    private lateinit var favoritesRepo: FavoritesRepo

    @Before
    fun setUp() = runTest {
        val appToRoomMapper = AppToRoomMapper()
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
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
    fun getOnlyFavoritesFlow() = runTest {
        val apps = listOf(TestApps.Chrome, TestApps.Phone)
        val job = launch {
            favoritesRepo.onlyFavoritesFlow.test {
                var appsList = awaitItem()
                assertThat(appsList).isEmpty()
                appsList = awaitItem()
                assertThat(appsList.size).isEqualTo(apps.size)
                assertThat(appsList).isEqualTo(apps)
                expectNoEvents()
            }
        }

        apps.forEach { favoritesRepo.addToFavorites(it) }
        job.join()
    }

    @Test
    fun addToFavorites() = runTest {
        val app = TestApps.Chrome
        val job = launch {
            favoritesRepo.onlyFavoritesFlow.test {
                val appsList = awaitItem()
                assertThat(appsList.size).isEqualTo(1)
                assertThat(appsList).isEqualTo(listOf(app))
                expectNoEvents()
            }
        }

        favoritesRepo.addToFavorites(app)
        job.join()
    }

    @Test
    fun reorderFavorite() = runTest {
        val initialFavorites = listOf(TestApps.Chrome, TestApps.Phone, TestApps.Youtube)
        initialFavorites.forEach { favoritesRepo.addToFavorites(it) }
        val reorderedFavorites = initialFavorites.asReversed()

        val job = launch {
            favoritesRepo.onlyFavoritesFlow.test {
                assertThat(awaitItem()).isEqualTo(reorderedFavorites)
                expectNoEvents()
            }
        }

        favoritesRepo.reorderFavorite(initialFavorites.first(), initialFavorites.last())
        job.join()
    }

    @Test
    fun removeFromFavorites() = runTest {
        val apps = listOf(TestApps.Chrome, TestApps.Phone)
        val appToRemove = apps.first()
        val appsAfterRemoving = apps.filter { it.packageName != appToRemove.packageName }

        val job = launch {
            favoritesRepo.onlyFavoritesFlow.test {
                var appsList = awaitItem()
                assertThat(appsList.size).isEqualTo(apps.size)
                assertThat(appsList).isEqualTo(apps)
                appsList = awaitItem()
                assertThat(appsList.size).isEqualTo(appsAfterRemoving.size)
                assertThat(appsList).isEqualTo(appsAfterRemoving)
                expectNoEvents()
            }
        }

        apps.forEach { favoritesRepo.addToFavorites(it) }
        favoritesRepo.removeFromFavorites(appToRemove.packageName)
        job.join()
    }

    @Test
    fun clearFavorites() = runTest {
        val apps = listOf(TestApps.Chrome, TestApps.Youtube)
        val job = launch {
            favoritesRepo.onlyFavoritesFlow.test {
                var appsList = awaitItem()
                assertThat(appsList.size).isEqualTo(apps.size)
                assertThat(appsList).isEqualTo(apps)
                appsList = awaitItem()
                assertThat(appsList).isEmpty()
                expectNoEvents()
            }
        }

        apps.forEach { favoritesRepo.addToFavorites(it) }
        favoritesRepo.clearFavorites()
        job.join()
    }

    @Test
    fun isFavorite() = runTest {
        val app = TestApps.Chrome
        favoritesRepo.addToFavorites(app)
        assertThat(favoritesRepo.isFavorite(app.packageName)).isTrue()
        favoritesRepo.removeFromFavorites(app.packageName)
        assertThat(favoritesRepo.isFavorite(app.packageName)).isFalse()
    }
}
