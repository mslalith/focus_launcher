package dev.mslalith.focuslauncher.core.data.repository.impl.settings

import dev.mslalith.focuslauncher.core.data.base.RepoTest
import dev.mslalith.focuslauncher.core.data.model.TestComponents
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_VIEW_TYPE
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_ICONS
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR
import dev.mslalith.focuslauncher.core.data.verifyChange
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
internal class AppDrawerSettingsRepoImplTest : RepoTest<AppDrawerSettingsRepoImpl>() {

    override fun provideRepo(testComponents: TestComponents) = AppDrawerSettingsRepoImpl(settingsDataStore = testComponents.dataStore)

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
