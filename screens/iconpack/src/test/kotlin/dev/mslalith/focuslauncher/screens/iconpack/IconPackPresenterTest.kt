package dev.mslalith.focuslauncher.screens.iconpack

import android.content.ComponentName
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import app.cash.turbine.ReceiveTurbine
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.test.TestAppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.model.LoadingState
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.domain.apps.GetAllAppsOnIconPackChangeUseCase
import dev.mslalith.focuslauncher.core.domain.apps.GetIconPackIconicAppsUseCase
import dev.mslalith.focuslauncher.core.domain.iconpack.FetchIconPacksUseCase
import dev.mslalith.focuslauncher.core.domain.iconpack.LoadIconPackUseCase
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.test.TestIconProvider
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithComponent
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import dev.mslalith.focuslauncher.core.testing.disableAsSystem
import dev.mslalith.focuslauncher.core.testing.launcherapps.TestIconPackManager
import dev.mslalith.focuslauncher.core.testing.toPackageNamed
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
class IconPackPresenterTest : PresenterTest<IconPackPresenter, IconPackState>() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getAllAppsOnIconPackChangeUseCase: GetAllAppsOnIconPackChangeUseCase

    @Inject
    lateinit var getIconPackIconicAppsUseCase: GetIconPackIconicAppsUseCase

    @Inject
    lateinit var generalSettingsRepo: GeneralSettingsRepo

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    private val testIconProvider = TestIconProvider
    private val testIconPackManager = TestIconPackManager()
    private val fetchIconPacksUseCase = FetchIconPacksUseCase(iconPackManager = testIconPackManager)
    private val loadIconPackUseCase = LoadIconPackUseCase(iconPackManager = testIconPackManager)

    private val appCoroutineDispatcher = TestAppCoroutineDispatcher()
    private val allApps by lazy { TestApps.all.toPackageNamed().disableAsSystem() }

    @Before
    fun setup() {
        hiltRule.inject()
        runBlocking {
            appDrawerRepo.addApps(apps = allApps)
        }
    }

    override fun presenterUnderTest() = IconPackPresenter(
        navigator = navigator,
        getAllAppsOnIconPackChangeUseCase = getAllAppsOnIconPackChangeUseCase,
        getIconPackIconicAppsUseCase = getIconPackIconicAppsUseCase,
        fetchIconPacksUseCase = fetchIconPacksUseCase,
        loadIconPackUseCase = loadIconPackUseCase,
        generalSettingsRepo = generalSettingsRepo,
        appCoroutineDispatcher = appCoroutineDispatcher
    )

    @Test
    fun `01 - when an icon pack is selected, state must be updated with it's new value`() = runPresenterTest {
        val selectedIconPackType = IconPackType.Custom(packageName = "com.test")

        assertThat(awaitItem().iconPackType).isNull()

        val state = awaitItem()
        assertThat(state.iconPackType).isEqualTo(IconPackType.System)

        state.eventSink(IconPackUiEvent.UpdateSelectedIconPackApp(iconPackType = selectedIconPackType))

        assertThat(awaitItem().iconPackType).isEqualTo(selectedIconPackType)
    }

    @Test
    fun `02 - when an icon pack is saved, state must be updated with it's new value`() = runPresenterTest {
        val selectedIconPackType = IconPackType.Custom(packageName = "com.test")

        assertThat(awaitItem().iconPackType).isNull()

        val state = awaitItem()
        assertThat(state.iconPackType).isEqualTo(IconPackType.System)

        state.eventSink(IconPackUiEvent.UpdateSelectedIconPackApp(iconPackType = selectedIconPackType))
        state.eventSink(IconPackUiEvent.SaveIconPack)

        assertThat(awaitItem().iconPackType).isEqualTo(selectedIconPackType)
    }

    @Test
    fun `03 - when icon pack is changed, all apps with icons should have selected icon pack icons`() = runPresenterTest {
        val selectedIconPackType = IconPackType.Custom(packageName = "com.test")
        testIconProvider.setIconColor(color = Color.CYAN)

        val state = awaitItem()
        assertThat(state.allApps).isEqualTo(LoadingState.Loading)
        assertThat(awaitItem().allApps).isEqualTo(LoadingState.Loading)

        assertAllApps(expected = allApps, iconPackType = IconPackType.System)

        testIconProvider.setIconColor(color = Color.BLUE)
        state.eventSink(IconPackUiEvent.UpdateSelectedIconPackApp(iconPackType = selectedIconPackType))

        assertThat(awaitItem().allApps).isEqualTo(LoadingState.Loading)
        assertThat(awaitItem().allApps).isEqualTo(LoadingState.Loading)
        assertAllApps(expected = allApps, iconPackType = selectedIconPackType)
    }

    context (ReceiveTurbine<IconPackState>)
    private suspend fun assertAllApps(expected: List<App>, iconPackType: IconPackType) {
        val state = assertFor(expected = true) { it.allApps is LoadingState.Loaded }
        val allAppsState = state.allApps
        check(allAppsState is LoadingState.Loaded)
        assertThat(allAppsState.value.size).isEqualTo(expected.size)

        val expectedWithIcon = with(testIconProvider) { expected.toAppDrawerItems(iconPackType = iconPackType) }
        allAppsState.value.zip(expectedWithIcon) { a, b ->
            assertThat(a compareWith b).isTrue()
        }
    }
}

context (TestIconProvider)
private fun List<App>.toAppDrawerItems(iconPackType: IconPackType): List<AppDrawerItem> = map { app ->
    AppDrawerItem(
        app = app,
        isFavorite = false,
        icon = iconFor(
            appWithComponent = AppWithComponent(
                app = app,
                componentName = ComponentName(app.packageName, app.packageName)
            ),
            iconPackType = iconPackType
        ),
        color = null
    )
}

private infix fun AppDrawerItem.compareWith(appDrawerItem: AppDrawerItem): Boolean {
    val thisIcon = icon
    val thatIcon = appDrawerItem.icon

    val iconEquality = when {
        thisIcon is ColorDrawable && thatIcon is ColorDrawable -> thisIcon.color == thatIcon.color
        else -> false
    }

    return app == appDrawerItem.app && isFavorite == appDrawerItem.isFavorite && color == appDrawerItem.color && iconEquality
}
