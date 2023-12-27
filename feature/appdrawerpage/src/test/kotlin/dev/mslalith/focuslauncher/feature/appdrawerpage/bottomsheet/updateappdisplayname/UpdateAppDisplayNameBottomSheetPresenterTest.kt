package dev.mslalith.focuslauncher.feature.appdrawerpage.bottomsheet.updateappdisplayname

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.screens.UpdateAppDisplayNameBottomSheetScreen
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AppRobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class UpdateAppDisplayNameBottomSheetPresenterTest : PresenterTest<UpdateAppDisplayNameBottomSheetPresenter, UpdateAppDisplayNameBottomSheetState>() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    @Inject
    lateinit var appCoroutineDispatcher: AppCoroutineDispatcher

    private val app = TestApps.Chrome

    @Before
    fun setUp() {
        hiltRule.inject()
        runBlocking {
            appDrawerRepo.addApps(apps = TestApps.all)
        }
    }

    override fun presenterUnderTest() = UpdateAppDisplayNameBottomSheetPresenter(
        screen = UpdateAppDisplayNameBottomSheetScreen(app = app),
        navigator = navigator,
        appDrawerRepo = appDrawerRepo,
        appCoroutineDispatcher = appCoroutineDispatcher
    )

    @Test
    fun `01 - when display is updated, new name should be shown`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.app).isEqualTo(app)

        val newName = "Test"
        state.eventSink(UpdateAppDisplayNameBottomSheetUiEvent.UpdateDisplayName(displayName = newName))

        navigator.awaitPop()
        assertThat(appDrawerRepo.getAppBy(app.packageName)).isEqualTo(app.copy(displayName = newName))
    }
}
