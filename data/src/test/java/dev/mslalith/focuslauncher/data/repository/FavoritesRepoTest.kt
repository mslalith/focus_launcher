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
import dev.mslalith.focuslauncher.data.dto.FavoriteToRoomMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private val appToRoomMapper = AppToRoomMapper()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries().build()
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

    private suspend fun addTestApps() {
        database.appsDao().addApps(TestApps.all.map(appToRoomMapper::toEntity))
    }

    @Test
    fun getOnlyFavoritesFlow() = runCoroutineTest {
        addTestApps()
        val testApps = listOf(TestApps.Chrome, TestApps.Phone)

        var apps = favoritesRepo.onlyFavoritesFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEmpty()

        favoritesRepo.addToFavorites(testApps)
        apps = favoritesRepo.onlyFavoritesFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEqualTo(testApps)
    }

    @Test
    fun addToFavorites() = runCoroutineTest {
        addTestApps()
        val testApp = TestApps.Chrome

        var apps = favoritesRepo.onlyFavoritesFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEmpty()

        favoritesRepo.addToFavorites(testApp)
        apps = favoritesRepo.onlyFavoritesFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEqualTo(listOf(testApp))
    }

    @Test
    fun reorderFavorite() = runCoroutineTest {
        addTestApps()
        val initialFavorites = listOf(TestApps.Chrome, TestApps.Phone, TestApps.Youtube)
        favoritesRepo.addToFavorites(initialFavorites)
        val reorderedFavorites = initialFavorites.asReversed()

        var apps = favoritesRepo.onlyFavoritesFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEqualTo(initialFavorites)

        favoritesRepo.reorderFavorite(initialFavorites.first(), initialFavorites.last())
        apps = favoritesRepo.onlyFavoritesFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEqualTo(reorderedFavorites)
    }

    @Test
    fun removeFromFavorites() = runCoroutineTest {
        addTestApps()
        val testApps = listOf(TestApps.Chrome, TestApps.Phone)
        val appToRemove = testApps.first()
        val appsAfterRemoving = testApps.filter { it.packageName != appToRemove.packageName }

        var apps = favoritesRepo.onlyFavoritesFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEmpty()

        favoritesRepo.addToFavorites(testApps)
        apps = favoritesRepo.onlyFavoritesFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEqualTo(testApps)

        favoritesRepo.removeFromFavorites(appToRemove.packageName)
        apps = favoritesRepo.onlyFavoritesFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEqualTo(appsAfterRemoving)
    }

    @Test
    fun clearFavorites() = runCoroutineTest {
        addTestApps()
        val testApps = listOf(TestApps.Chrome, TestApps.Youtube)

        var apps = favoritesRepo.onlyFavoritesFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEmpty()

        favoritesRepo.addToFavorites(testApps)
        apps = favoritesRepo.onlyFavoritesFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEqualTo(testApps)

        favoritesRepo.clearFavorites()
        apps = favoritesRepo.onlyFavoritesFlow.testIn(this).awaitItemAndCancel()
        assertThat(apps).isEmpty()
    }

    @Test
    fun isFavorite() = runCoroutineTest {
        addTestApps()
        val app = TestApps.Chrome

        favoritesRepo.addToFavorites(app)
        assertThat(favoritesRepo.isFavorite(app.packageName)).isTrue()

        favoritesRepo.removeFromFavorites(app.packageName)
        assertThat(favoritesRepo.isFavorite(app.packageName)).isFalse()
    }
}
