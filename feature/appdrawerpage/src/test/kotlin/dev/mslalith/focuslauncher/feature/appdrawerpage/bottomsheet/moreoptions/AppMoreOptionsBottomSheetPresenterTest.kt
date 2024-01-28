package dev.mslalith.focuslauncher.feature.appdrawerpage.bottomsheet.moreoptions

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.circuitoverlay.OverlayResultScreen
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.test.TestAppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.test.repository.FakeAppDrawerRepo
import dev.mslalith.focuslauncher.core.data.test.repository.FakeFavoritesRepo
import dev.mslalith.focuslauncher.core.data.test.repository.FakeHiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import dev.mslalith.focuslauncher.core.screens.AppMoreOptionsBottomSheetScreen
import dev.mslalith.focuslauncher.core.screens.AppMoreOptionsBottomSheetScreen.Result.ShowUpdateAppDisplayBottomSheet
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import kotlinx.coroutines.runBlocking
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AppRobolectricTestRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class AppMoreOptionsBottomSheetPresenterTest : PresenterTest<AppMoreOptionsBottomSheetPresenter, AppMoreOptionsBottomSheetState>() {

    private val appDrawerRepo = FakeAppDrawerRepo()
    private val favoritesRepo = FakeFavoritesRepo(fakeAppDrawerRepo = appDrawerRepo)
    private val hiddenAppsRepo = FakeHiddenAppsRepo(fakeAppDrawerRepo = appDrawerRepo)
    private val appCoroutineDispatcher = TestAppCoroutineDispatcher()

    private var isFavorite = false

    override fun presenterUnderTest() = AppMoreOptionsBottomSheetPresenter(
        screen = AppMoreOptionsBottomSheetScreen(appDrawerItem = appDrawerItem()),
        navigator = navigator,
        favoritesRepo = favoritesRepo,
        hiddenAppsRepo = hiddenAppsRepo,
        appCoroutineDispatcher = appCoroutineDispatcher
    )

    @Test
    fun `01 - when app is added to favorite, it should be saved`() {
        isFavorite = false

        runPresenterTest {
            val state = awaitItem()
            val app = state.appDrawerItem.app

            assertThat(favoritesRepo.isFavorite(packageName = app.packageName)).isFalse()

            state.eventSink(AppMoreOptionsBottomSheetUiEvent.AddToFavorites(app = app))
            assertThat(favoritesRepo.isFavorite(packageName = app.packageName)).isTrue()

            navigator.awaitPop()
        }
    }

    @Test
    fun `02 - when app is removed from favorites, it should be removed`() = runBlocking {
        isFavorite = true
        favoritesRepo.addToFavorites(app = appDrawerItem().app)

        runPresenterTest {
            val state = awaitItem()
            val app = state.appDrawerItem.app

            assertThat(favoritesRepo.isFavorite(packageName = app.packageName)).isTrue()

            state.eventSink(AppMoreOptionsBottomSheetUiEvent.RemoveFromFavorites(app = app))
            assertThat(favoritesRepo.isFavorite(packageName = app.packageName)).isFalse()

            navigator.awaitPop()
        }
    }

    @Test
    fun `03 - when an app is hidden, it should be saved`() {
        isFavorite = false

        runPresenterTest {
            val state = awaitItem()
            val app = state.appDrawerItem.app

            assertThat(hiddenAppsRepo.isHidden(packageName = app.packageName)).isFalse()

            state.eventSink(AppMoreOptionsBottomSheetUiEvent.AddToHiddenApps(app = app, removeFromFavorites = isFavorite))
            assertThat(hiddenAppsRepo.isHidden(packageName = app.packageName)).isTrue()

            navigator.awaitPop()
        }
    }

    @Test
    fun `04 - when a favorite app is hidden, it should be removed from favorites and hidden`() = runBlocking {
        isFavorite = true
        favoritesRepo.addToFavorites(app = appDrawerItem().app)

        runPresenterTest {
            val state = awaitItem()
            val app = state.appDrawerItem.app

            assertThat(favoritesRepo.isFavorite(packageName = app.packageName)).isTrue()
            assertThat(hiddenAppsRepo.isHidden(packageName = app.packageName)).isFalse()

            state.eventSink(AppMoreOptionsBottomSheetUiEvent.AddToHiddenApps(app = app, removeFromFavorites = isFavorite))
            assertThat(favoritesRepo.isFavorite(packageName = app.packageName)).isFalse()
            assertThat(hiddenAppsRepo.isHidden(packageName = app.packageName)).isTrue()

            navigator.awaitPop()
        }
    }

    @Test
    fun `05 - when update display name is clicked, verify bottom sheet event`() = runPresenterTest {
        isFavorite = false

        val state = awaitItem()
        val appDrawerItem = appDrawerItem()

        state.eventSink(AppMoreOptionsBottomSheetUiEvent.ClickUpdateDisplayName(app = appDrawerItem.app))

        @Suppress("UNCHECKED_CAST")
        val nextScreen = navigator.awaitNextScreen() as OverlayResultScreen<ShowUpdateAppDisplayBottomSheet>
        assertThat(nextScreen.result).isEqualTo(ShowUpdateAppDisplayBottomSheet)
    }

    private fun appDrawerItem(): AppDrawerItem = AppDrawerItem(
        app = TestApps.Chrome,
        isFavorite = isFavorite,
        icon = ColorDrawable(Color.RED),
        color = null
    )
}
