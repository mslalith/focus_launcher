package dev.mslalith.focuslauncher.core.data.repository.impl

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.data.base.RepoTest
import dev.mslalith.focuslauncher.core.data.model.TestComponents
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
internal class FavoritesRepoImplTest : RepoTest<FavoritesRepoImpl>() {

    override fun provideRepo(testComponents: TestComponents): FavoritesRepoImpl {
        return FavoritesRepoImpl(
            appsDao = testComponents.database.appsDao(),
            favoriteAppsDao = testComponents.database.favoriteAppsDao(),
            appToRoomMapper = testComponents.mappers.appToRoomMapper,
            favoriteToRoomMapper = testComponents.mappers.favoriteToRoomMapper
        )
    }

    @Before
    override fun setUp() = runBlocking {
        testComponents.database.appsDao().addApps(TestApps.all.map(testComponents.mappers.appToRoomMapper::toEntity))
    }

    @Test
    fun `initially favorites must be empty`() = runCoroutineTest {
        val items = repo.onlyFavoritesFlow.awaitItem()
        assertThat(items).isEmpty()
    }

    @Test
    fun `when an app is added to favorite, make sure it stays as favorite`() = runCoroutineTest {
        val app = TestApps.Chrome
        repo.addToFavorites(app)

        val items = repo.onlyFavoritesFlow.awaitItem()
        assertThat(items).isEqualTo(listOf(app))
    }

    @Test
    fun `when multiple apps are added to favorites, make sure they stays as favorites`() = runCoroutineTest {
        val apps = listOf(TestApps.Chrome, TestApps.Phone)
        repo.addToFavorites(apps)

        val items = repo.onlyFavoritesFlow.awaitItem()
        assertThat(items).isEqualTo(apps)
    }

    @Test
    fun `when an app is removed from favorites, make sure it isn't present`() = runCoroutineTest {
        val app = TestApps.Chrome
        repo.addToFavorites(app)

        var items = repo.onlyFavoritesFlow.awaitItem()
        assertThat(items).isEqualTo(listOf(app))

        repo.removeFromFavorites(app.packageName)

        items = repo.onlyFavoritesFlow.awaitItem()
        assertThat(items).doesNotContain(app)
        assertThat(items).isEmpty()
    }

    @Test
    fun `when favorites are cleared, list should be empty`() = runCoroutineTest {
        val apps = TestApps.all
        repo.addToFavorites(apps)

        var items = repo.onlyFavoritesFlow.awaitItem()
        assertThat(items).isEqualTo(apps)

        repo.clearFavorites()

        items = repo.onlyFavoritesFlow.awaitItem()
        assertThat(items).isEmpty()
    }

    @Test
    fun `when querying for a favorite app, isFavorite must return true`() = runCoroutineTest {
        val app = TestApps.Youtube
        repo.addToFavorites(app)

        val items = repo.onlyFavoritesFlow.awaitItem()
        assertThat(items).isEqualTo(listOf(app))

        val isHidden = repo.isFavorite(app.packageName)
        assertThat(isHidden).isTrue()
    }

    @Test
    fun `when querying for an un-favorite app, isFavorite must return false`() = runCoroutineTest {
        val app = TestApps.Chrome
        repo.addToFavorites(app)

        val items = repo.onlyFavoritesFlow.awaitItem()
        assertThat(items).isEqualTo(listOf(app))

        val isHidden = repo.isFavorite(TestApps.Phone.packageName)
        assertThat(isHidden).isFalse()
    }

    @Test
    fun `when 2 favorites are present and are reordered, make sure it returns reordered list`() = runCoroutineTest {
        val appsBeforeReorder = listOf(TestApps.Phone, TestApps.Youtube)
        repo.addToFavorites(appsBeforeReorder)

        var items = repo.onlyFavoritesFlow.awaitItem()
        assertThat(items).isEqualTo(appsBeforeReorder)

        repo.reorderFavorite(TestApps.Youtube, TestApps.Phone)
        val appsAfterReorder = listOf(TestApps.Youtube, TestApps.Phone)

        items = repo.onlyFavoritesFlow.awaitItem()
        assertThat(items).isEqualTo(appsAfterReorder)
    }

    @Test
    fun `when more than 2 favorites are present and two of them are reordered, make sure it returns reordered list`() = runCoroutineTest {
        val appsBeforeReorder = listOf(TestApps.Phone, TestApps.Chrome, TestApps.Youtube)
        repo.addToFavorites(appsBeforeReorder)

        var items = repo.onlyFavoritesFlow.awaitItem()
        assertThat(items).isEqualTo(appsBeforeReorder)

        repo.reorderFavorite(TestApps.Youtube, TestApps.Phone)
        val appsAfterReorder = listOf(TestApps.Youtube, TestApps.Chrome, TestApps.Phone)

        items = repo.onlyFavoritesFlow.awaitItem()
        assertThat(items).isEqualTo(appsAfterReorder)
    }
}
