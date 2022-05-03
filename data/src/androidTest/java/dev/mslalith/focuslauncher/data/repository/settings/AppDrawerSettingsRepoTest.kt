package dev.mslalith.focuslauncher.data.repository.settings

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.androidtest.shared.DataStoreTest
import dev.mslalith.focuslauncher.data.model.AppDrawerViewType
import dev.mslalith.focuslauncher.data.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppDrawerSettingsRepoTest : DataStoreTest<AppDrawerSettingsRepo>(
    setupRepo = { AppDrawerSettingsRepo(it) }
) {

    @Test
    fun getAppDrawerViewTypeFlow() = runTest {
        val value = repo.appDrawerViewTypeFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_VIEW_TYPE)
    }

    @Test
    fun getAppIconsVisibilityFlow() = runTest {
        val value = repo.appIconsVisibilityFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_ICONS)
    }

    @Test
    fun getSearchBarVisibilityFlow() = runTest {
        val value = repo.searchBarVisibilityFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR)
    }

    @Test
    fun getAppGroupHeaderVisibilityFlow() = runTest {
        val value = repo.appGroupHeaderVisibilityFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER)
    }

    @Test
    fun updateAppDrawerViewType() = runTest {
        AppDrawerViewType.values().forEach { viewType ->
            repo.updateAppDrawerViewType(viewType)
            val value = repo.appDrawerViewTypeFlow.first()
            assertThat(value).isEqualTo(viewType)
        }
    }

    @Test
    fun toggleAppIconsVisibility() = runTest {
        repo.toggleAppIconsVisibility()
        val value = repo.appIconsVisibilityFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_ICONS)
    }

    @Test
    fun toggleSearchBarVisibility() = runTest {
        repo.toggleSearchBarVisibility()
        val value = repo.searchBarVisibilityFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR)
    }

    @Test
    fun toggleAppGroupHeaderVisibility() = runTest {
        repo.toggleAppGroupHeaderVisibility()
        val value = repo.appGroupHeaderVisibilityFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER)
    }
}
