package dev.mslalith.focuslauncher.screens.iconpack

import android.content.ComponentName
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import app.cash.turbine.TurbineContext
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.model.getOrNull
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
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.disableAsSystem
import dev.mslalith.focuslauncher.core.testing.extensions.assertFor
import dev.mslalith.focuslauncher.core.testing.launcherapps.TestIconPackManager
import dev.mslalith.focuslauncher.core.testing.toPackageNamed
import dev.mslalith.focuslauncher.screens.iconpack.model.IconPackState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class IconPackViewModelTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getAllAppsOnIconPackChangeUseCase: GetAllAppsOnIconPackChangeUseCase

    @Inject
    lateinit var getIconPackIconicAppsUseCase: GetIconPackIconicAppsUseCase

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    @Inject
    lateinit var generalSettingsRepo: GeneralSettingsRepo

    @Inject
    lateinit var appCoroutineDispatcher: AppCoroutineDispatcher

    private lateinit var viewModel: IconPackViewModel

    private val testIconProvider = TestIconProvider
    private val testIconPackManager = TestIconPackManager()
    private val fetchIconPacksUseCase = FetchIconPacksUseCase(iconPackManager = testIconPackManager)
    private val loadIconPackUseCase = LoadIconPackUseCase(iconPackManager = testIconPackManager)

    private val allApps by lazy { TestApps.all.toPackageNamed().disableAsSystem() }

    @Before
    fun setup() {
        hiltRule.inject()
        testIconPackManager.hashCode()
        viewModel = IconPackViewModel(
            getAllAppsOnIconPackChangeUseCase = getAllAppsOnIconPackChangeUseCase,
            getIconPackIconicAppsUseCase = getIconPackIconicAppsUseCase,
            fetchIconPacksUseCase = fetchIconPacksUseCase,
            loadIconPackUseCase = loadIconPackUseCase,
            generalSettingsRepo = generalSettingsRepo,
            appCoroutineDispatcher = appCoroutineDispatcher
        )
        runBlocking {
            appDrawerRepo.addApps(apps = allApps)
        }
    }

    @Test
    fun `01 - when an icon pack is selected, state must be updated with it's new value`() = runCoroutineTest {
        val selectedIconPackType = IconPackType.Custom(packageName = "com.test")

        viewModel.iconPackState.assertFor(expected = IconPackType.System) { it.iconPackType }

        viewModel.updateSelectedIconPackApp(iconPackType = selectedIconPackType)

        viewModel.iconPackState.assertFor(expected = selectedIconPackType) { it.iconPackType }
    }

    @Test
    fun `02 - when an icon pack is saved, state must be updated with it's new value`() = runCoroutineTest {
        val selectedIconPackType = IconPackType.Custom(packageName = "com.test")

        viewModel.iconPackState.assertFor(expected = IconPackType.System) { it.iconPackType }

        viewModel.updateSelectedIconPackApp(iconPackType = selectedIconPackType)
        viewModel.saveIconPackType()

        viewModel.iconPackState.assertFor(expected = selectedIconPackType) { it.iconPackType }
    }

    @Test
    fun `03 - when icon pack is changed, all apps with icons should have selected icon pack icons`() = runCoroutineTest {
        val selectedIconPackType = IconPackType.Custom(packageName = "com.test")

        testIconProvider.setIconColor(color = Color.CYAN)
        viewModel.iconPackState.assertAllAppsWith(apps = allApps, iconPackType = IconPackType.System)

        viewModel.updateSelectedIconPackApp(iconPackType = selectedIconPackType)
        testIconProvider.setIconColor(color = Color.BLUE)

        viewModel.iconPackState.assertAllAppsWith(apps = allApps, iconPackType = selectedIconPackType)
    }

    context (TurbineContext)
    private suspend fun StateFlow<IconPackState>.assertAllAppsWith(
        apps: List<App>,
        iconPackType: IconPackType
    ) = with(testIconProvider) { assertAllAppsWith(expected = apps.toAppDrawerItems(iconPackType = iconPackType)) }
}

context (TurbineContext)
private suspend fun StateFlow<IconPackState>.assertAllAppsWith(
    expected: List<AppDrawerItem>
) {
    assertFor(
        expected = expected,
        valueFor = { it.allApps.getOrNull() },
        compare = { a, b -> a compareWith b },
        assertion = { assertThat(it compareWith expected).isTrue() }
    )
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

private infix fun List<AppDrawerItem>?.compareWith(appDrawerItems: List<AppDrawerItem>?): Boolean {
    if (this == null && appDrawerItems == null) return true
    if (this == null || appDrawerItems == null) return false
    if (size != appDrawerItems.size) return false

    val thisSortedList = sortedBy { it.app.packageName }
    val thatSortedList = appDrawerItems.sortedBy { it.app.packageName }
    return thisSortedList.zip(other = thatSortedList).all { (thisItem, thatItem) ->
        thisItem compareWith thatItem
    }
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
