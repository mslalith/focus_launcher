package dev.mslalith.focuslauncher.feature.appdrawerpage

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.AppDrawerSettingsRepo
import dev.mslalith.focuslauncher.core.domain.apps.GetAppDrawerIconicAppsUseCase
import dev.mslalith.focuslauncher.core.domain.iconpack.ReloadIconPackAfterFirstLoadUseCase
import dev.mslalith.focuslauncher.core.domain.iconpack.ReloadIconPackUseCase
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Ignore
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
class AppDrawerPagePresenterTest : PresenterTest<AppDrawerPagePresenter, AppDrawerPageState>() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getAppDrawerIconicAppsUseCase: GetAppDrawerIconicAppsUseCase

    @Inject
    lateinit var reloadIconPackAfterFirstLoadUseCase: ReloadIconPackAfterFirstLoadUseCase

    @Inject
    lateinit var appDrawerSettingsRepo: AppDrawerSettingsRepo

    @Inject
    lateinit var reloadIconPackUseCase: ReloadIconPackUseCase

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    @Inject
    lateinit var appCoroutineDispatcher: AppCoroutineDispatcher

    @Before
    fun setUp() {
        hiltRule.inject()
        runBlocking {
            appDrawerRepo.addApps(apps = TestApps.all)
        }
    }

    override fun presenterUnderTest() = AppDrawerPagePresenter(
        getAppDrawerIconicAppsUseCase = getAppDrawerIconicAppsUseCase,
        reloadIconPackAfterFirstLoadUseCase = reloadIconPackAfterFirstLoadUseCase,
        appDrawerSettingsRepo = appDrawerSettingsRepo,
        reloadIconPackUseCase = reloadIconPackUseCase,
        appCoroutineDispatcher = appCoroutineDispatcher
    )

    @Test
    @Ignore("ignoring due to LocalOverlayHost")
    fun `01 - when user searches, query should be updated`() = runPresenterTest(initialValue = mockk()) {
        val state = awaitItem()
        assertThat(state.searchBarQuery).isEmpty()

        val query = "chrome"
        state.eventSink(AppDrawerPageUiEvent.UpdateSearchQuery(query = query))
        val awaitItem = awaitItem()
        assertThat(awaitItem.searchBarQuery).isEqualTo(query)
    }
}
