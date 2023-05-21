package dev.mslalith.focuslauncher.core.data.repository.settings

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.data.helpers.verifyChange
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_VIEW_TYPE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_ICONS
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import javax.inject.Inject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
internal class AppDrawerSettingsRepoTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: AppDrawerSettingsRepo

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun `verify app drawer view type change`() = runCoroutineTest {
        verifyChange(
            flow = repo.appDrawerViewTypeFlow,
            initialItem = DEFAULT_APP_DRAWER_VIEW_TYPE,
            mutate = {
                val newItem = AppDrawerViewType.LIST
                repo.updateAppDrawerViewType(newItem)
                newItem
            }
        )
    }

    @Test
    fun `verify app icon visibility change`() = runCoroutineTest {
        verifyChange(
            flow = repo.appIconsVisibilityFlow,
            initialItem = DEFAULT_APP_ICONS,
            mutate = {
                repo.toggleAppIconsVisibility()
                false
            }
        )
    }

    @Test
    fun `verify search bar visibility change`() = runCoroutineTest {
        verifyChange(
            flow = repo.searchBarVisibilityFlow,
            initialItem = DEFAULT_SEARCH_BAR,
            mutate = {
                repo.toggleSearchBarVisibility()
                false
            }
        )
    }

    @Test
    fun `verify app group header visibility change`() = runCoroutineTest {
        verifyChange(
            flow = repo.appGroupHeaderVisibilityFlow,
            initialItem = DEFAULT_APP_GROUP_HEADER,
            mutate = {
                repo.toggleAppGroupHeaderVisibility()
                false
            }
        )
    }
}
